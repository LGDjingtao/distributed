import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class OptionalTest {
    @Test
    public void testSplitOnSplit() {
        Optional<Integer> possible = Optional.of(5);

        possible.isPresent(); // returns true

        possible.get(); // returns 5
    }
}
