/**
 * Created by burkhart on 10/8/16.
 */
public interface IStructureInterface extends IEntity {
    String getComplexName();
    String getResidues(String chain);
    int getSize(String chain);
}
