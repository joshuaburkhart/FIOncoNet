import org.biojava.nbio.structure.*;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.contact.AtomContactSet;
import org.biojava.nbio.structure.contact.GroupContact;
import org.biojava.nbio.structure.contact.GroupContactSet;

import java.util.*;
import java.util.List;

/**
 * Created by burkhart on 10/8/16.
 */
public class StructureInterface extends Entity implements IStructureInterface {
    final double INTERFACE_THRESHOLD = 5.0d;
    final String CHAIN_A = "A";
    final String CHAIN_B = "B";
    final String FIELD_DELIM = ", ";
    final String RESNUM_DELIM = " ";

    private String complexName;
    private Map<String,Collection<Integer>> residueNumbers;
    private Structure structure;
    private ILogger logger;

    public StructureInterface(ILogger logger, Structure structure, String complexName){
        this.logger = logger;
        this.structure = structure;
        this.complexName = complexName;
        this.residueNumbers = new HashMap<>();
        extractInterfaceData();
    }

    private void extractInterfaceData(){
        Chain chainA = null;
        Chain chainB = null;
        try {
            chainA = this.structure.getChainByPDB(CHAIN_A);
            chainB = this.structure.getChainByPDB(CHAIN_B);
        } catch (StructureException se) {
            this.logger.Log(LoggingLevel.ERROR,"structure exception getting chain",se);
        }
        if(chainA != null) {
            if(chainB != null) {
                AtomContactSet contacts = StructureTools.getAtomsInContact(
                        chainA,
                        chainB,
                        INTERFACE_THRESHOLD,
                        false);
                GroupContactSet groupSet = new GroupContactSet(contacts);
                for (GroupContact contact : groupSet) {
                    // Chain A
                    processChain(contact.getPair().getFirst());

                    // Chain B
                    processChain(contact.getPair().getSecond());
                }
            }else{
                this.logger.Log(LoggingLevel.WARNING,"null chainB detected in structure '" + getComplexName() + "'");
            }
        }else{
            this.logger.Log(LoggingLevel.WARNING,"null chainA detected in structure '" + getComplexName() + "'");
        }
    }

    private void processChain(Group grp){
        if(!this.residueNumbers.keySet().contains(grp.getChainId())){
            this.residueNumbers.put(grp.getChain().getChainID(),new HashSet<>());
        }

        this.residueNumbers.get(grp.getChainId()).add(grp.getResidueNumber().getSeqNum());
    }

    @Override
    public String ToString() {
        return getComplexName() + FIELD_DELIM +
                getResidues(CHAIN_A) + FIELD_DELIM +
                getResidues(CHAIN_B) + FIELD_DELIM +
                getSize(CHAIN_A) + FIELD_DELIM +
                getSize(CHAIN_B);
    }

    @Override
    public String getComplexName() {
        return this.complexName;
    }

    private List<Integer> getResidueList(String chain){
        List<Integer> residues = new ArrayList<>();
        if(this.residueNumbers.containsKey(chain)){
            residues.addAll(this.residueNumbers.get(chain));
        }
        Collections.sort(residues);
        return residues;
    }

    @Override
    public String getResidues(String chain) {
        StringBuilder sb = new StringBuilder();
        for (Integer i : getResidueList(chain))
        {
            sb.append(i);
            sb.append(RESNUM_DELIM);
        }
        return sb.toString();
    }

    @Override
    public int getSize(String chain) {
        return getResidueList(chain).size();
    }
}
