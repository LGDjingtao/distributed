import com.google.common.base.Joiner;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;


import static java.util.stream.Collectors.joining;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class JoinerTest {
    private final List<String> stringList = Arrays.asList(
            "Goole", "Guava", "Java", "Scala", "Kafka"
    );
    private final List<String> stringListWithNullValue = Arrays.asList(
            "Goole", "Guava", "Java", "Scala", "Kafka", null
    );


    /**
     * {@link Joiner#on(java.lang.String) }
     * {@link Joiner#join(java.lang.Iterable) }
     * <p>连接可以迭代的对象 {@link java.lang.Iterable}</p>
     */
    @Test
    public void testJionOnJoin(){
        String result = Joiner.on("#").join(stringList);
        assertThat(result,equalTo("Goole#Guava#Java#Scala#Kafka"));
    }

    /**
     * {@link com.google.common.base.Joiner#skipNulls() }
     * <p>可以跳过null元素</p>
     */
    @Test
    public void testJionOnJoinWithNullValueButSkip(){
        String result = Joiner.on("#").skipNulls().join(stringListWithNullValue);
        assertThat(result,equalTo("Goole#Guava#Java#Scala#Kafka"));
    }

    /**
     * {@link com.google.common.base.Joiner#useForNull(java.lang.String)}
     * <p>null元素使用某个默认值替代,这里使用 "DEFAULT" 替代 null<p/>
     */
    @Test
    public void testJionOnJoinWithNullValue_UseDefaulttValue(){
        String result = Joiner.on("#").useForNull("DEFAULT").join(stringListWithNullValue);
        assertThat(result,equalTo("Goole#Guava#Java#Scala#Kafka#DEFAULT"));

        //不能为了用guava而用,java8也可以很容易实现这种方法
        String collect = stringListWithNullValue.stream().map(this::defaultValue).collect(joining("#"));
        System.out.println(collect);
    }

    private String defaultValue(String str){
        return str == null || str.isEmpty() ? "DEFAULT" : str;
    }

    /**
     * {@link  com.google.common.base.Joiner#appendTo(java.lang.StringBuilder, java.lang.Iterable)}
     * <p>字符串追加<p/>
     */
    @Test
    public void testJionOnAppendTo(){
        final StringBuilder builder = new StringBuilder();
        StringBuilder resultBuilder =  Joiner.on("#").useForNull("DEFAULT").appendTo(builder,stringListWithNullValue);

        assertThat(resultBuilder,sameInstance(builder));
        assertThat(resultBuilder.toString(),equalTo("Goole#Guava#Java#Scala#Kafka#DEFAULT"));
    }
}
