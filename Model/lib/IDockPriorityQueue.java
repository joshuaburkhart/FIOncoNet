import javafx.util.Pair;

import java.util.List;

/**
 * Created by burkhart on 6/7/16.
 */
public interface IDockPriorityQueue extends IPriorityQueue {
    java.util.List<IDockStructure> DockStructures = null;
    List<Pair<IIsoform, IIsoform>> DockQueue = null;
}
