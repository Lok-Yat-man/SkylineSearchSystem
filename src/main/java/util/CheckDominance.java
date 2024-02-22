package util;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.HasGeometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.geometry.internal.GeometryUtil;
import com.github.davidmoten.rtree.geometry.internal.RectangleDouble;
import com.github.davidmoten.rtree.internal.EntryDefault;
import entity.Coordinate;
import entity.Query;
import entity.RelevantObject;
import service.DefaultRelevantObjectServiceImpl;

import java.util.List;


public class CheckDominance {
    public static boolean isDominant(Entry<String, Geometry> s, Entry<String, Geometry> e, List<Query> queries) {
        Rectangle Ru = null;
        DefaultRelevantObjectServiceImpl droService = new DefaultRelevantObjectServiceImpl();

        //对每个query
        for (Query q : queries) {
            List<String> ss = droService.getById(s.value()).getWeightKey();
//            System.out.println(ss);
            if (!ss.containsAll(q.getKeywords())) {
                return true;
            }
            double qx = q.getLocation().getLongitude();
            double qy = q.getLocation().getLatitude();
            double radius = st(s, q);
            Rectangle Ruqi = RectangleDouble.create(qx - radius, qy - radius, qx + radius, qy + radius);
            Ru = Ru.add(Ruqi);
        }
        String eId = e.value();
        RelevantObject objectE = droService.getById(eId);
        return Ru.contains(objectE.getCoordinate().getLongitude(), objectE.getCoordinate().getLatitude());
    }



    public static double st(HasGeometry e, Query query) {
        double logQ = query.getLocation().getLongitude();
        double latQ = query.getLocation().getLatitude();
        Coordinate coordinateQ = Coordinate.create(logQ, latQ);

        double logE = e.geometry().mbr().x1();
        double latE = e.geometry().mbr().y1();
        Coordinate coordinateE = Coordinate.create(logE, latE);
        double dist = CommonAlgorithm.calculateDistance(coordinateE, coordinateQ);

        return dist;
    }
}
