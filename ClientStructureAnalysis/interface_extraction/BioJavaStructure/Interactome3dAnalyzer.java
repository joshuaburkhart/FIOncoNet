/*
 * Created on Sep 22, 2016
 *
 */
package org.reactome.r3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.StructureTools;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.contact.AtomContactSet;
import org.biojava.nbio.structure.contact.GroupContact;
import org.biojava.nbio.structure.contact.GroupContactSet;
import org.biojava.nbio.structure.contact.Pair;
import org.gk.util.GKApplicationUtilities;
import org.junit.Test;
import org.reactome.px.util.InteractionUtilities;
import org.reactome.r3.ProteinSequenceHandler.Sequence;
import org.reactome.r3.util.FileUtility;

/**
 * This class is used to handle interaction structure data downloaded from interactome3d.
 * @author gwu
 *
 */
public class Interactome3dAnalyzer {

    private final double INTERFACE_THRESHOLD = 5.0d; // The unit is Angstrom (A)
    private FileUtility fu = new FileUtility();

    /**
     * Default constructor.
     */
    public Interactome3dAnalyzer() {
    }

    /**
     * Compare if there are overlapping structures for FIs in two interaction3d queries.
     * @throws IOException
     */
    @Test
    public void compareTwoSetsOfInteractions() throws IOException {
        String dirName = "datasets/interactome3d/2016_06/reactions_fdr_01/";
        List<File> files01 = getInteractionStructureFiles(dirName + "complete",
                                                                true);
        System.out.println(dirName);
        System.out.println("Total files: " + files01.size());
        Set<String> fis01 = new HashSet<String>();
        for (File file : files01)
            fis01.add(extractFIFromFileName(file));
        System.out.println("Total FIs: " + fis01.size());
        dirName = "datasets/interactome3d/2016_06/reactions_fdr_05_01/";
        List<File> files05_01 = getInteractionStructureFiles(dirName + "complete",
                                                             true);
        System.out.println(dirName);
        System.out.println("Total fils: " + files05_01.size());
        Set<String> fis05_01 = new HashSet<String>();
        for (File file : files05_01)
            fis05_01.add(extractFIFromFileName(file));
        System.out.println("Total FIs: " + fis05_01.size());
        Set<String> shared = InteractionUtilities.getShared(fis01, fis05_01);
        System.out.println("Shared interactions: " + shared.size());
    }

    private Map<String, Integer> loadRank(String dir) throws IOException {
        String fileName = dir + File.separator + "interactions.dat";
        fu.setInput(fileName);
        String line = fu.readLine();
        Map<String, Integer> fileToRank = new HashMap<String, Integer>();
        while ((line = fu.readLine()) != null) {
            String[] tokens = line.split("\t");
            fileToRank.put(tokens[tokens.length - 1],
                           new Integer(tokens[2]));
        }
        fu.close();
        return fileToRank;
    }

    @Test
    public void pickUpBestStructuresBasedOnContacts() throws Exception {
        // To hold all picked PDBs
        String targetDir = "datasets/interactome3d/2016_06/reactions_fdr_05/";
        // Source dir
        String dirName = "datasets/interactome3d/2016_06/reactions_fdr_01/";
        dirName = "datasets/interactome3d/2016_06/reactions_fdr_05_01/";
//        String representDir = dirName + "representative";
//        List<File> representFiles = getInteractionStructureFiles(representDir,
//                                                                 true);
//        System.out.println("Total interactions in " + dirName + ": ");
//        System.out.println("\trepresentative: " + representFiles.size());
        String completeDir = dirName + "complete";
        Map<String, Integer> fileToRank = loadRank(completeDir);
        List<File> completeFiles = getInteractionStructureFiles(completeDir,
                                                                true);
        System.out.println("\tcomplete: " + completeFiles.size());

        List<File> allFiles = new ArrayList<File>();
//        allFiles.addAll(representFiles);
        allFiles.addAll(completeFiles);
        Map<String, List<InteractionPDBScore>> fiToScores = new HashMap<String, List<InteractionPDBScore>>();
        for (File file : allFiles) {
            InteractionPDBScore score = calculateInteractionScore(file,
                                                                  fileToRank);
            String fi = extractFIFromFileName(file);
            List<InteractionPDBScore> scores = fiToScores.get(fi);
            if (scores == null) {
                scores = new ArrayList<InteractionPDBScore>();
                fiToScores.put(fi, scores);
            }
            scores.add(score);
        }
        System.out.println("Total FIs: " + fiToScores.size());
        StringBuilder builder = new StringBuilder();
        for (String fi : fiToScores.keySet()) {
            List<InteractionPDBScore> scores = fiToScores.get(fi);
            Collections.sort(scores);
            builder.append(fi).append("\t");
            for (InteractionPDBScore score : scores) {
                builder.append(score.getShortFileName()).append("\t");
//                builder.append(score.isInRepresent).append("\t");
                builder.append(score.interactResidues).append("\t");
            }
            builder.deleteCharAt(builder.length() - 1);
            System.out.println(builder.toString());
            builder.setLength(0);
            // Copy the top one
            InteractionPDBScore topScore = scores.get(0);
            File targetFile = new File(targetDir, topScore.getShortFileName());
            GKApplicationUtilities.copy(new File(topScore.fileName),
                                        targetFile);
        }
    }

    public void setUpAtomCache() {
        AtomCache cache = new AtomCache();
        cache.setUseMmCif(true);
        StructureIO.setAtomCache(cache);
    }

    private InteractionPDBScore calculateInteractionScore(File file,
                                                          Map<String, Integer> fileToRank) throws Exception {
        Structure structure = StructureIO.getStructure(file.getAbsolutePath());
        Map<Chain, List<Integer>> chainToResidues = extractContacts(structure);
        int total = 0;
        for (Chain chain : chainToResidues.keySet()) {
            List<Integer> list = chainToResidues.get(chain);
            total += list.size(); // all interactions scored equally
        }
        // Get the total residues in the interface
        // fi unused?
        String fi = extractFIFromFileName(file);
        InteractionPDBScore score = new InteractionPDBScore();
        score.fileName = file.getAbsolutePath();
        score.interactResidues = total;
        score.isInRepresent = file.getAbsolutePath().contains("representative");
        score.rank = fileToRank.get(file.getName());
        return score;
    }

    @Test
    public void testLoadPPIToPDBFile() throws IOException {
        String dirName = "datasets/interactome3d/2016_06/prebuilt/representative/";
        Map<String, File> ppiToPDB = loadPPIToPDBFile(dirName, false);
        System.out.println("Total PPIToPDB: " + ppiToPDB.size());
    }

    private List<File> getInteractionStructureFiles(String dirName,
                                                     boolean recursive) throws IOException {
        List<File> interactionPDBFiles = new ArrayList<File>();
        List<File> dirs = new ArrayList<File>();
        dirs.add(new File(dirName));
        List<File> next = new ArrayList<File>();
        // At least once
        while (dirs.size() > 0) {
            for (File dir : dirs) {
                File[] files = dir.listFiles();
                if (files == null || files.length == 0)
                    continue;
                for (File file : files) {
                    if (file.isDirectory()) {
                        next.add(file);
                        continue;
                    }
                    if (!file.getName().endsWith(".pdb"))
                        continue;
                    String parentDirName = file.getParentFile().getName();
                    if (!parentDirName.startsWith("interactions"))
                        continue; // We will see interaction PDB only
                    interactionPDBFiles.add(file);
                }
            }
            if (!recursive)
                break; // Perform once only
            dirs.clear();
            dirs.addAll(next);
            next.clear();
        }
        return interactionPDBFiles;
    }

    public Map<String, File> loadPPIToPDBFile(String dirName,
                                              boolean recursive) throws IOException {
        Map<String, File> ppiToPDB = new HashMap<String, File>();
        List<File> interactionPDBFiles = getInteractionStructureFiles(dirName,
                                                                      recursive);
        for (File file : interactionPDBFiles) {
            // Do a simple parsing
            String fi = extractFIFromFileName(file);
            ppiToPDB.put(fi, file);
        }
        return ppiToPDB;
    }

    private String extractFIFromFileName(File file) {
        String[] tokens = file.getName().split("-");
        String fi = InteractionUtilities.generateFIFromGene(tokens[0], tokens[1]);
        return fi;
    }

    @Test
    public void downloadData() throws Exception {
//        String dirName = "datasets/interactome3d/2016_06/representative/";
//        String hostUrl = "http://interactome3d.irbbarcelona.org/user_data/human/download/representative/";
//
//        dirName = "datasets/interactome3d/2016_06/complete/";
//        hostUrl = "http://interactome3d.irbbarcelona.org/user_data/human/download/complete/";
        // Results for reactions having enrichment fdrs <= 0.01
        // Representative
        String dirName = "datasets/interactome3d/2016_06/reactions_fdr_01/representative/";
        String hostUrl = "http://interactome3d.irbbarcelona.org/user_data/EXggwX2u6XZQVKWMpuBQ/download/representative/";
        int largestInt = 1;
        int largestProtein = 2;
        // Complete
        dirName = "datasets/interactome3d/2016_06/reactions_fdr_01/complete/";
        hostUrl = "http://interactome3d.irbbarcelona.org/user_data/EXggwX2u6XZQVKWMpuBQ/download/complete/";
        largestInt = 2;
        largestProtein = 26;
        // Results for reactions having encirhment fdr [0.05, 0.01)
        // representative
        dirName = "datasets/interactome3d/2016_06/reactions_fdr_05_01/representative/";
        hostUrl = "http://interactome3d.irbbarcelona.org/user_data/CEtGowWWxcDu6bxu5rm6/download/representative/";
        largestInt = 2;
        largestProtein = 2;
        // Complete
        dirName = "datasets/interactome3d/2016_06/reactions_fdr_05_01/complete/";
        hostUrl = "http://interactome3d.irbbarcelona.org/user_data/CEtGowWWxcDu6bxu5rm6/download/complete/";
        largestInt = 7;
        largestProtein = 28;

        String fileName = "interactions.dat";
        download(dirName, hostUrl, fileName);
        fileName = "proteins.dat";
        download(dirName, hostUrl, fileName);
        downloadDataFiles(dirName, hostUrl, "interactions", largestInt);
        downloadDataFiles(dirName, hostUrl, "proteins", largestProtein);
        // After downloading, use a simple script as following:
        // $ for f in *.tar; do tar xf $f; done (http://stackoverflow.com/questions/583889/how-can-you-untar-more-than-one-file-at-a-time)
        // Or use the following script (preferred) to keep the original folder
//        for f in *.tgz; do d=`basename $f .tgz`; mkdir $d; cd $d; tar xzvf ../$f; cd ..; done
    }

    private void downloadDataFiles(String dirName,
                                   String hostUrl,
                                   String dataType,
                                   int largestInt) throws Exception {
        for (int i = 1; i <= largestInt; i++) {
            String inset = "";
            if (largestInt >= 100) {
                if (i < 10)
                    inset = "00";
                else if (i < 100)
                    inset = "0";
            }
            else if (largestInt >= 10) {
                if (i < 10)
                    inset = "0";
            }
            String fileName = dataType + "_" + inset + i + ".tgz";
            download(dirName, hostUrl, fileName);
        }
    }

    private void download(String dirName, String hostUrl, String fileName) throws Exception {
        System.out.println("Download: " + fileName);
        String dataUrl = hostUrl + fileName;
        URL url = new URL(dataUrl);
        InputStream is = url.openStream();
        FileOutputStream fos = new FileOutputStream(dirName + fileName);
        byte[] buffer = new byte[1024 * 100];
        int read = is.read(buffer, 0, buffer.length);
        while (read > 0) {
            fos.write(buffer, 0, read);
            read = is.read(buffer, 0, buffer.length);
        }
        is.close();
        fos.close();
    }

    @Test
    public void checkInterfaces() throws Exception {
        String dir = "datasets/interactome3d/2016_06/reactions_fdr_01/complete/interactions_2/";
        String fileName = dir + "P20393-Q07869-MDL-1a6y.pdb1-B-0-A-0.pdb";
        setUpAtomCache();
        Structure structure = StructureIO.getStructure(fileName);
        Map<Chain, List<Integer>> chainToResidues = extractContacts(structure);
        for (Chain chain : chainToResidues.keySet()) {
            System.out.println(chain.getChainID());
            List<Integer> residues = chainToResidues.get(chain);
            System.out.println(residues);
        }
    }

    public Map<Chain, List<Integer>> extractContacts(Structure structure) throws StructureException {
        // Assume there are two chains, named as A and B. This seems always true for
        // PDBs downloaded from Interactome3D.
        Chain chainA = structure.getChainByPDB("A");
        Chain chainB = structure.getChainByPDB("B");

#        AtomContactSet contacts = StructureTools.getAtomsInContact(chainA,
                                                                   chainB,
                                                                   INTERFACE_THRESHOLD,
                                                                   false);
        GroupContactSet groupSet = new GroupContactSet(contacts);
        Map<Chain, Set<Integer>> chainToCoordinates = new HashMap<Chain, Set<Integer>>();
        /*
        should probably break this out by contact..
        should these be separate file lines in the report?
        currently, coordinates are just extracted & packed into a Map keyed by chain only 
        */
        for (GroupContact contact : groupSet) {
            Pair<Group> pair = contact.getPair();
            // contact coordinates from A chain
            Set<Integer> coordinates = chainToCoordinates.get(pair.getFirst().getChain());

            /*
             =======
             CHAIN A
             =======
            */
            // put empty coordinates into chain-coord object if null
            if (coordinates == null) {
                coordinates = new HashSet<Integer>();
                chainToCoordinates.put(pair.getFirst().getChain(),
                                       coordinates);
            }
            // add chain A sequence coordinates
            coordinates.add(pair.getFirst().getResidueNumber().getSeqNum());

            /*
             =======
             CHAIN B
             =======
            */
            coordinates = chainToCoordinates.get(pair.getSecond().getChain());
            if (coordinates == null) {
                coordinates = new HashSet<Integer>();
                chainToCoordinates.put(pair.getSecond().getChain(),
                                       coordinates);
            }
            coordinates.add(pair.getSecond().getResidueNumber().getSeqNum());
        }
        Map<Chain, List<Integer>> rtn = new HashMap<Chain, List<Integer>>();
        // just A & B chains?
        for (Chain chain : chainToCoordinates.keySet()) {
            Set<Integer> set = chainToCoordinates.get(chain);
            List<Integer> list = new ArrayList<Integer>(set);
            Collections.sort(list);
            rtn.put(chain, list);
        }
        /* returning map like:
        Map["ChainA"] -> HashSet{1,2,3,4,6,7,8,9,...} // HashSet contains seq coords
        Map["ChainB"] -> HashSet{4,5,6,7,10,11,12,...}
        */
        return rtn;
    }

    public Map<Chain, PDBUniProtMatch> getMatchForExpStructure(Structure structure,
                                                                String[] accessions,
                                                                Map<String, String> accToGene) throws StructureException {
        if (accessions.length < 2)
            throw new IllegalArgumentException("The passed accessions array should have at least 2 elements!");
        Map<Chain, PDBUniProtMatch> chainToMatch = new HashMap<Chain, PDBUniProtMatch>();
        for (int i = 0; i < 2; i++) {
            String chainId = (i == 0 ? "A" : "B");
            Chain chain = structure.getChainByPDB(chainId);
            PDBUniProtMatch match = new PDBUniProtMatch();
            match.chain = chainId;
            match.uniprot = accessions[i];
            match.gene = accToGene.get(match.uniprot);
            match.offset = 0;
            chainToMatch.put(chain, match);
        }
        return chainToMatch;
    }

    public Map<Chain, PDBUniProtMatch> mapCoordinatesToUniProtInPDB(Structure structure,
                                                                    String[] acces,
                                                                    Map<String, Sequence> accToSeq,
                                                                    Map<String, String> accToGene) throws IOException {
        List<Chain> chains = structure.getChains();

        Map<Chain, PDBUniProtMatch> chainToMatch = new HashMap<Chain, PDBUniProtMatch>();
        for (Chain chain : chains) {
            for (String acc : acces) {
                Sequence seq = accToSeq.get("UniProt:" + acc);
//                System.out.println(acc + ": " + seq.getSequence());
                int index = seq.getSequence().indexOf(chain.getAtomSequence());
                if (index >= 0) {
//                    System.out.println(seq.getSequence());
//                    System.out.println(chain.getAtomSequence());
                    PDBUniProtMatch match = new PDBUniProtMatch();
                    match.chain = chain.getChainID();
                    match.gene = accToGene.get(acc);
                    match.uniprot = acc;
                    match.pdbStart = chain.getAtomGroup(0).getResidueNumber().getSeqNum();
                    match.uniprotStart = index + 1;
                    match.offset = match.uniprotStart - match.pdbStart;
                    chainToMatch.put(chain, match);
                }
            }
        }
        return chainToMatch;
    }

    public static class PDBUniProtMatch {

        private String chain;
        private String uniprot;
        private String gene;
        private int pdbStart;
        private int uniprotStart;
        private int offset; // Add this number to pdb cooridnate to get UniProt coordiate

        public PDBUniProtMatch() {

        }

        public String getChain() {
            return chain;
        }

        public String getUniprot() {
            return uniprot;
        }

        public String getGene() {
            return gene;
        }

        public int getPdbStart() {
            return pdbStart;
        }

        public int getUniprotStart() {
            return uniprotStart;
        }

        public int getOffset() {
            return offset;
        }
    }

    private class InteractionPDBScore implements Comparable<InteractionPDBScore> {

        private boolean isInRepresent;
        private String fileName;
        private int interactResidues;
        private String fi;
        private int rank;

        @Override
        public int compareTo(InteractionPDBScore o) {
            if (interactResidues != o.interactResidues)
                return o.interactResidues - interactResidues;
            return o.rank - rank;
        }

        public String getShortFileName() {
            int index = fileName.lastIndexOf(File.separator);
            if (index >= 0)
                return fileName.substring(index + 1);
            return fileName;
        }

    }

}
