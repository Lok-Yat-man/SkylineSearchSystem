package util;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.HasGeometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.geometry.internal.GeometryUtil;
import com.github.davidmoten.rtree.geometry.internal.RectangleDouble;
import com.github.davidmoten.rtree.internal.EntryDefault;
import com.github.davidmoten.rtree.internal.NonLeafDefault;
import entity.Coordinate;
import entity.Query;
import entity.RelevantObject;
import service.DefaultRelevantObjectServiceImpl;
import std.BSTD;

import java.util.List;



public class CheckDominance {
    public static Rectangle skylinesMBR(List<Entry<String, Geometry>> S, List<Query> queries) {
//        for (Entry<String, Geometry> s : S) {
//            Rectangle Rui = null;
//            for (Query q : queries) {
//                double qx = q.getLocation().getLongitude();
//                double qy = q.getLocation().getLatitude();
//                double radius = st(s, q);
//                Rectangle temp = Geometries.rectangleGeographic(qx - radius, qy - radius,
//                        qx + radius, qy + radius);
//                Rui = Rui.add(temp);
//            }
//        }
        return Geometries.rectangleGeographic(0, 0, 0, 0);
    }



}
