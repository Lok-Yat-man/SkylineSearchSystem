package std;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.Node;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.HasGeometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.geometry.internal.RectangleDouble;
import com.github.davidmoten.rtree.internal.LeafDefault;
import com.github.davidmoten.rtree.internal.NonLeafDefault;
import entity.Query;
import entity.RelevantObject;
import irtree.IRTree;
import ivtidx.DefaultLeafInvertedIndex;
import ivtidx.InvertedIndex;
import service.DefaultRelevantObjectServiceImpl;
import service.IRelevantObjectService;
import util.MBRIntersection;

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
        // S=∅; B=U
        List<Entry<String, Geometry>> S = new LinkedList<>();
        Optional<? extends Node<String, Geometry>> rootOptional = irTree.getRTree().root();
        if (!rootOptional.isPresent()) {
            throw new RuntimeException("RTree not exists!");
        }
        Node<String, Geometry> rootNode = rootOptional.get();
        Rectangle B = RectangleDouble.create(-180, 0, 180, 90);

        /*System.out.println(rootNode.geometry());
        System.out.println(((NonLeafDefault<String, Geometry>) rootNode).children().size());
        for (Node node:
        ((NonLeafDefault<String, Geometry>) rootNode).children()) {
            System.out.println(node.geometry());
        }*/

        // MinHeap H=∅
        // Add root of IRTree to H, ∑(qi∈Q)〖st(qi,p)〗
        PriorityQueue<Node<String, Geometry>> minHeap = new PriorityQueue<>((o1, o2) -> {
            double stSum1 = 0.0, stSum2 = 0.0;
            for (Query query : queries) {
                stSum1 += st(o1, query);
                stSum2 += st(o2, query);
            }
            return Double.compare(stSum1, stSum2);
        });
        minHeap.add(rootNode);

        int countEmpty = 0, countCore = 0, countNon = 0;

        while (!minHeap.isEmpty()) {

            //System.out.println("minHeap" + minHeap.size());

            Node<String, Geometry> e = minHeap.poll();
            if (e.geometry().mbr().intersects(B)) {
                // e is a leaf node
                if (e instanceof LeafDefault) {

                    System.out.println(e.getClass().getName());

                    if (S.isEmpty()) {
                        // Add e to S
                        // B = B ∩ Ru(e)
                        S.addAll(((LeafDefault<String, Geometry>) e).entries());
                        B = MBRIntersection.getIntersectMBR(B, e.geometry().mbr());

                        System.out.println("countEmpty" + (++countEmpty));
                        System.out.println("B " + B);

                    } else {
                        // for each entry ∈ LeafNode
                        for (Entry<String, Geometry> ee : ((LeafDefault<String, Geometry>) e).entries()) {
                            boolean isSkyline = true;
                            // for each p ∈ S
                            for (Entry<String, Geometry> s : S) {
                                if (isDominant(s, ee, queries)) {
                                    isSkyline = false;
                                    break;
                                }
                            }
                            if (isSkyline) {
                                S.add(ee);
                                B = MBRIntersection.getIntersectMBR(B, ee.geometry().mbr());
                            }
                        }

                        System.out.println("countCore" + (++countCore));

                    }
                }
                // e is a non-leaf node
                else if (e instanceof NonLeafDefault) {

                    System.out.println(e.getClass().getName());

                    for (Node<String, Geometry> ee : ((NonLeafDefault<String, Geometry>) e).children()) {
                        if (ee.geometry().mbr().intersects(B)) {
                            minHeap.add(ee);
                        }
                    }

                    //System.out.println("countNon" + (++countNon));
                }
            }
        }
        return S;
    }
}
