import bstd.BSTD;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.geometry.Geometry;
import entity.Coordinate;
import entity.Query;
import irtree.IRTree;
import org.junit.jupiter.api.Test;
import service.DefaultRelevantObjectServiceImpl;
import service.IRelevantObjectService;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BSTDTest {
    private BSTD b = new BSTD();
    @Test
    public void bstdTest(){
        LinkedList<Query> queries = new LinkedList<>();
        Query query = Query.create(
                Coordinate.create(
                        -75.16256713867188,
                        39.94322204589844
                ),
                Arrays.asList("Restaurants"),
                //Arrays.asList("Water"),
                5,
                60.0,
                3
        );
        queries.add(query);
        List<String> values = b.bstd(queries).stream()
                .map(entry->entry.value())
                .collect(Collectors.toList());
        System.out.println(values);
        IRelevantObjectService relevantObjectService = new DefaultRelevantObjectServiceImpl();

        System.out.println(relevantObjectService.getByIds(values));
    }
}
