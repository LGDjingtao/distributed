#logback使用笔记
整理一个可以用在不是很复杂的项目上的llogback日志框架使用

##引入依赖
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-test</artifactId>
</dependency>
为什么引入的是spring-boot-starter-test,这里介绍一个查看一个依赖引用其他间接依赖的方法
使用maven的命令
mvn dependency:tree>d:\tree.txt
可以将项目的依赖信息输入到文件夹,这里我将logback这个模块的依赖输入到d:\tree.txt 文件夹里查看得知
[INFO] +- org.springframework.boot:spring-boot-starter-test:jar:2.3.2.RELEASE:test
[INFO] |  +- org.springframework.boot:spring-boot-starter:jar:2.3.2.RELEASE:compile
[INFO] |  |  +- org.springframework.boot:spring-boot-starter-logging:jar:2.3.2.RELEASE:compile
[INFO] |  |  |  +- ch.qos.logback:logback-classic:jar:1.2.3:compile
[INFO] |  |  |  |  \- ch.qos.logback:logback-core:jar:1.2.3:compile

##编写logback.xml
详情查看resources目录
里面的每个元素基本都有注释

后期会更新多环境使用logback的方法(后期更新)



