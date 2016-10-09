
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by burkhart on 10/8/16.
 */
public class UniprotPdbMapFileParser extends TextFileParser{
    private ILogger logger;
    private String pdb_dir;

    public UniprotPdbMapFileParser(ILogger logger,String pdb_dir){
        this.logger = logger;
        this.pdb_dir = pdb_dir;
    }

    @Override
    public Collection<IEntity> ParseEntitiesFromFile(ITextFile uniprotPdbMapFile) {
        Collection<IEntity> modeled_genes = new HashSet<>();
        for(java.lang.String line : uniprotPdbMapFile.GetLines()){
                java.lang.String[] toks = line.split("\\s");
                java.lang.String geneSymbol = toks[0].toUpperCase().trim();
                IGene g = new Gene(geneSymbol);
                IIsoform i = new Isoform(g,pdb_dir + toks[13]);
                g.SetPrincipleIsoform(i);
                modeled_genes.add(g);
            }
        return modeled_genes;
    }
}
