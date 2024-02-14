package util;

import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.geometry.internal.RectangleDouble;

public class MBRIntersection {

    public static Rectangle getIntersectMBR(Rectangle r1, Rectangle r2) {
        return RectangleDouble.create(max(r1.x1(), r2.x1()), max(r1.y1(), r2.y1()), min(r1.x2(), r2.x2()), min(r1.y2(), r2.y2()));
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
