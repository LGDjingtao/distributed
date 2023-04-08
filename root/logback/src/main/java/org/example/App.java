package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class App 
{

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public void testLevel(){
        logger.trace(" --- trace --- ");
        logger.debug(" --- debug --- ");
        logger.info(" --- info --- ");
        logger.warn(" --- warn --- ");
        logger.error(" --- error --- ");
        try {
            int k = 0/1;
            int k2 = 1/0;
        }catch (Exception e){
            //为什么这样打印错误信息,不要用e.printStackTrace(),e.printStackTrace()这个破方法会使线程阻塞
            logger.error(" {}",e.toString()); //打印报错信息
            Arrays.stream(e.getStackTrace()).forEach(v-> logger.error(" {}",v.toString())); //打印堆栈信息
        }

    }

    public static void main( String[] args )
    {
        App a = new App();
        a.testLevel();

        System.out.println( "Hello World!" );
    }
}
