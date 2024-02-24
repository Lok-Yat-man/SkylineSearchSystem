package util;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.geometry.internal.RectangleDouble;
import entity.Query;
import std.BSTD;

public class MBR {

    public static Rectangle getIntersectMBR(Rectangle r1, Rectangle r2) {
        return Geometries.rectangleGeographic(max(r1.x1(), r2.x1()), max(r1.y1(), r2.y1()),
                min(r1.x2(), r2.x2()), min(r1.y2(), r2.y2()));
    }

    public static Rectangle generateEntryMBR (Entry<String, Geometry> e, Query query) {
        double qx = query.getLocation().getLongitude();
        double qy = query.getLocation().getLatitude();
        // //todo
        double radius = 0;
        return Geometries.rectangle(qx - radius, qy - radius, qx + radius, qy + radius);
    }

    private static double max(double a, double b) {
        if (a < b)
            return b;
        else
            return a;
    }

    private static double min(double a, double b) {
        if (a < b)
            return a;
        else
            return b;
    }
}
