package ivtidx;

import entity.Coordinate;

import java.util.*;

public interface InvertedIndex<T> {

    List<T> getValues(String s);
    List<T> getValues(List<String> ss);
}
