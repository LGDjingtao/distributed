package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
public class TestController {
    @Value("${user.kk:}")
    String us;

    @Value("${user.kk3:}")
    String us2;


    @GetMapping("a")
    public String output(){
        System.out.println(us);
        return us;
    }

    @GetMapping("b")
    public String outputb(){
        System.out.println(us2);
        return us2;
    }
}
