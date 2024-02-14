package bstd;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.Node;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.HasGeometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.internal.LeafDefault;
import com.github.davidmoten.rtree.internal.NonLeafDefault;
import entity.Query;
import entity.RelevantObject;
import irtree.IRTree;
import ivtidx.DefaultLeafInvertedIndex;
import ivtidx.InvertedIndex;
import service.DefaultRelevantObjectServiceImpl;
import service.IRelevantObjectService;

import java.util.*;

import static util.CheckDominance.isDominant;
import static util.CheckDominance.st;
import static util.MBRIntersection.getIntersectMBR;

public class BSTD {

    private IRTree irTree;

    private InvertedIndex<RelevantObject> invertedIndex;

    public BSTD() {

        IRelevantObjectService relevantObjectService = new DefaultRelevantObjectServiceImpl();

        invertedIndex = new DefaultLeafInvertedIndex(relevantObjectService);

        irTree = new IRTree(relevantObjectService);

    }

    public BSTD(IRTree irTree, InvertedIndex<RelevantObject> invertedIndex) {
        this.irTree = irTree;
        this.invertedIndex = invertedIndex;
    }

    public List<Entry<String, Geometry>> bstd(List<Query> queries) {
        List<Entry<String, Geometry>> S = new LinkedList<>();

        Optional<? extends Node<String, Geometry>> rootOptional = irTree.getRTree().root();
        if (!rootOptional.isPresent()) {
            throw new RuntimeException("RTree not exists!");
        }
        Node<String, Geometry> rootNode = rootOptional.get();
        Rectangle B = rootNode.geometry().mbr();

        PriorityQueue<Node> minHeap = new PriorityQueue<>((o1, o2) -> {
            double stSum1 = 0.0, stSum2 = 0.0;
            for (Query query : queries) {
                stSum1 += st(o1, query);
                stSum2 += st(o2, query);
            }
            return Double.compare(stSum1, stSum2);
        });
        minHeap.add(rootNode);

        while (!minHeap.isEmpty()) {
            HasGeometry e = minHeap.poll();
            if (e.geometry().mbr().intersects(B)) {
                if (e instanceof LeafDefault) {
                    if (S.isEmpty()) {
                        // Add e to S
                        S.addAll(((LeafDefault<String, Geometry>) e).entries());
                        B = getIntersectMBR(B, e.geometry().mbr());
                    } else {
                        for (Entry ee : ((LeafDefault<?, ?>) e).entries()) {
                            boolean isSkyline = true;
                            for (Entry s : S) {
                                if (isDominant(s, ee, queries)) {
                                    isSkyline = false;
                                    break;
                                }
                            }
                            if (isSkyline) {
                                S.add(ee);
                                B = getIntersectMBR(B, ee.geometry().mbr());
                            }
                        }
                    }
                } else if (e instanceof NonLeafDefault) {
                    for (Node<String, Geometry> ee : ((NonLeafDefault<String, Geometry>) e).children()) {
                        if (ee.geometry().mbr().intersects(B)) {
                            minHeap.add(ee);
                        }
                    }
                }
            }
        }
        return S;
    }
}
