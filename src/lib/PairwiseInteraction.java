package lib;

/**
 * Created by burkhart on 6/7/16.
 */
public class PairwiseInteraction extends Entity implements IPairwiseInteraction {
    private String gene1,gene2,annotation,direction,score;

    public PairwiseInteraction(String gene1,String gene2, String annotation, String direction, String score){
        this.gene1 = gene1;
        this.gene2 = gene2;
        this.annotation = annotation;
        this.direction = direction;
        this.score = score;
    }
}
