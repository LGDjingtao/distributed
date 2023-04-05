package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Value("${user.kk:}") //验证从nacos配置中心获取的指定配置文件
    String us;

    @Value("${user.kk3:}") //验证nacos拿去公共的配置文件
    String us2;

    @Value("${user.env:}") //验证spring.profiles.active = [环境名dev或者test] 指定不同的环境启动
    String env;



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

    @GetMapping("c")
    public String outputenv(){
        System.out.println(env);
        return env;
    }
}
