import ivtidx.DefaultLeafInvertedIndex;
import org.junit.jupiter.api.Test;
import service.DefaultRelevantObjectServiceImpl;
import service.IRelevantObjectService;

import java.util.LinkedList;
import java.util.List;

public class InvFileTest {
    @Test
    public void testInvFile(){
        IRelevantObjectService relevantObjectService = new DefaultRelevantObjectServiceImpl();
        DefaultLeafInvertedIndex defaultLeafInvertedIndex = new DefaultLeafInvertedIndex(relevantObjectService);
        List<String> strings = new LinkedList<>();
        strings.add("Balloons");
        strings.add("Surf");

        //System.out.println(relevantObjectService.getWeightsById("GldIUU-hF_Oq_zN4BE9EAg"));
        //System.out.println(defaultLeafInvertedIndex.getValues(strings));

    }
}
