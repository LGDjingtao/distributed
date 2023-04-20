import com.google.common.base.Splitter;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class SplitterTest {

    @Test
    public void testSplitOnSplit() {
        List<String> strings = Splitter.on("|").splitToList("hello|world");
        System.out.println(strings);
        List<String> strings1 = Splitter.on("|").splitToList("hello | world|||");
        System.out.println(strings1);
        List<String> strings2 = Splitter.on("|").omitEmptyStrings().splitToList("hello | world|||");
        System.out.println(strings2);
        List<String> strings3 = Splitter.on("|").trimResults().omitEmptyStrings().splitToList("hello | world|||");
        System.out.println(strings3);
        List<String> strings4 = Splitter.fixedLength(4).splitToList("aaaabbbbcccc");
        System.out.println(strings4);
        List<String> strings5 = Splitter.on("|").limit(3).splitToList("hello|world|aaaa|vvvvv|bbbb");
        System.out.println(strings5);
        //还可以传正则表达式
        List<String> strings6 = Splitter.onPattern("\\|").trimResults().omitEmptyStrings().splitToList("hello | world|||");
        System.out.println(strings6);
        Map<String, String> split = Splitter.onPattern("\\|").trimResults().omitEmptyStrings().withKeyValueSeparator("=").split("hello=zjt | world=stc|||");
        System.out.println(split);
    }


}
