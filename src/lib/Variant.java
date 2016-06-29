package lib;

/**
 * Created by burkhart on 6/7/16.
 */
public class Variant extends Entity implements IVariant{
    private String chrom,pos,id,ref,alt,qual,filter;

    public Variant(String chrom, String pos, String id, String ref, String alt, String qual, String filter){
        this.chrom = chrom;
        this.pos = pos;
        this.id = id;
        this.ref = ref;
        this.alt = alt;
        this.qual = qual;
        this.filter = filter;
    }
}
