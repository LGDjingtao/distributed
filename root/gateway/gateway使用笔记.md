#gateway使用笔记

#使用前提:已经使用docker部署好了 mysql 和 nacos 到服务器上 ,本地也启动了服务nacos并且注册到注册中心

##指定maven父项目
目的是获取母项目的依赖版本
<parent>
    <groupId>org.example</groupId>
    <artifactId>root</artifactId>
    <version>1.0-SNAPSHOT</version>
</parent>

##引入gateway依赖
详情查看 pom文件



##创建配置文件
bootstrap.yml 这个yml里面有详细注释,这里不详细说明



