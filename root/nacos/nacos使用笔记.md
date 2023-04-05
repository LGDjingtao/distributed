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
