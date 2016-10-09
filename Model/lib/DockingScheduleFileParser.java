

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by burkhart on 10/8/16.
 */
public class DockingScheduleFileParser extends XlsxFileParser {
    private ILogger logger;
    private Map<String,IGene> modeled_genes;
    private int uniprot_1_idx;
    private int uniprot_2_idx;
    private int gene_1_idx;
    private int gene_2_idx;
    private int pos_feat_idx;
    private String parentDirectory;

    public DockingScheduleFileParser(ILogger logger,
                                     Map<String,IGene> modeled_genes,
                                     int uniprot_1_idx,
                                     int uniprot_2_idx,
                                     int gene_1_idx,
                                     int gene_2_idx,
                                     int pos_feat_idx,
                                     String parentDirectory){
        this.logger = logger;
        this.modeled_genes = modeled_genes;
        this.uniprot_1_idx = uniprot_1_idx;
        this.uniprot_2_idx = uniprot_2_idx;
        this.gene_1_idx = gene_1_idx;
        this.gene_2_idx = gene_2_idx;
        this.pos_feat_idx = pos_feat_idx;
        this.parentDirectory = parentDirectory;
    }

    @Override
    public Collection<IEntity> ParseEntitiesFromFile(IXlsxFile entityFile) {
        Collection<IEntity> scheduled_jobs = new HashSet<>();
        for(Row row:entityFile.GetRows()){
            if(row.getCell(this.pos_feat_idx).getCellTypeEnum() == CellType.BOOLEAN){
                String uniprot1 = row.getCell(this.uniprot_1_idx).getStringCellValue();
                String uniprot2 = row.getCell(this.uniprot_2_idx).getStringCellValue();
                String gene1 = row.getCell(this.gene_1_idx).getStringCellValue();
                String gene2 = row.getCell(this.gene_2_idx).getStringCellValue();
                this.logger.Log(LoggingLevel.INFO,"parsed xlsx docking: " +
                        uniprot1 + ", " +
                        uniprot2 + ", " +
                        gene1 + ", " +
                        gene2);
                //ignore gene1 & 2 for now.. perhaps add these to a
                //yet unimplemented GeneID class
                String geneSymbol1 = uniprot1.toUpperCase().trim();
                String geneSymbol2 = uniprot2.toUpperCase().trim();
                if(this.modeled_genes.containsKey(geneSymbol1)){
                    if(this.modeled_genes.containsKey(geneSymbol2)) {
                        scheduled_jobs.add(new DockingJob(
                                this.modeled_genes.get(geneSymbol1).GetPrincipleIsoform(),
                                this.modeled_genes.get(geneSymbol2).GetPrincipleIsoform(),
                                this.logger,
                                this.parentDirectory));
                    }else{
                        this.logger.Log(LoggingLevel.ERROR,"can't find a model for " + geneSymbol2);
                    }
                }else{
                    this.logger.Log(LoggingLevel.ERROR,"can't find a model for " + geneSymbol1);
                }
            }
        }
        return scheduled_jobs;
    }
}
