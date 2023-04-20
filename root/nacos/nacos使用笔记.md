# nacos使用笔记

# 使用前提:已经使用docker部署好了 mysql 和 nacos 到服务器上

## 指定maven父项目
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

## 关于部署项目使用那个环境的问题
在dockerfile里面可以指定
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar", "--spring.profiles.active=[环境名dev或是test]"]
关于这是关于dockerfile的知识,
但是核心逻辑是改变配置文件的这一项
spring.profiles.active = [环境名dev或者test]

## nacos开发环境部署遇到注册IP地址混乱的情况怎么处理？
加入如下配置就可以防止开发环境注册服务ip混乱
spring.cloud.nacos.discovery.ip:192.168.1.1[当前主机使用的ip]
spring.cloud.nacos.discovery.prot:8080[当前服务使用的端口]

## nacos的服务状态不能及时更新解决方案
curl -X DELETE 'http://127.0.0.1:8848/nacos/v1/ns/instance?serviceName=gzrb-energy&groupName=DEFAULT_GROUP&namespaceId=gzrb_dev&ip=192.168.29.1&clustee=DEFAULT&port=9004&ephemeral=false'  
使用这个命令可以直接删除注册的服务  
gzrb-energy:服务名  
gzrb_dev:命名空间
DEFAULT_GROUP：默认组  
192.168.29.1本机的ip，根据自身情况进行更改  
9004：服务开放的端口，根据自身情况进行更改  
