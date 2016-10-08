/**
 * Created by burkhart on 6/7/16.
 */
public interface IVariant extends IEntity {
    String GetGeneSymbol();

    IGene GetGene();

    void SetGene(IGene gene);
}
