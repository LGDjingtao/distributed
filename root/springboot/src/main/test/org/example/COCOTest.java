package org.example;

import org.example.aop.annotation.COCO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class COCOTest {

    @Test
    public void test1(){
        tset2();

    }
    @COCO(name = "郑景涛",num = 18)
    public void tset2(){
        System.out.println("asdad");
    }
}
