#nacos使用笔记

#使用前提:已经使用docker部署好了 mysql 和 nacos 到服务器上

##指定maven父项目
目的是获取母项目的依赖版本
<parent>
    <groupId>org.example</groupId>
    <artifactId>root</artifactId>
    <version>1.0-SNAPSHOT</version>
</parent>

##引入nacos依赖
<dependency>
  <groupId>com.alibaba.cloud</groupId>
  <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
<dependency>
  <groupId>com.alibaba.cloud</groupId>
  <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>

##创建配置文件
bootstrap.yml 这个yml里面有详细注释,这里不详细说明,重点是看怎么注册到nacos和怎么获取nacos的指定配置

##关于部署项目使用那个环境的问题
在dockerfile里面可以指定
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar", "--spring.profiles.active=[环境名dev或是test]"]
关于这是关于dockerfile的知识,
但是核心逻辑是改变配置文件的这一项
spring.profiles.active = [环境名dev或者test]

