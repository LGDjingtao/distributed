# 分布式部署
`入职新公司,决定学习公司的项目,从0到1,构建一个完整的分布式项目
但是目前有些中间件是单机版,后期会完善集群版的部署,这个项目是以脚手架为目的去搭建的,
配置尽可能的齐全,每个点尽可能的会有说明和注释,这个文档的顺序就是我从0到1的构建顺序,
部署中间遇到的坑我也会在今后慢慢补充.
`

`以下的安装和部署全部基于docker`
`所有安装的中间件的挂载目录都在cd /data/[中间件名字] 这个目录下`
`所有的容器都用docker的host网络模式
所以以下的启动命令-p 这行可以不要`
`能持久化的挂载全部挂载,不只是简简单单的部署看看效果`

##Mysql 的安装
1.拉取mysql镜像
docker pull mysql
这里拉取的是最新的镜像

2.服务器上建立如下文件夹用于挂载
/data/mysql/conf
/data/mysql/data

3.创建容器
docker run -p 3306:3306 \
-v /data/mysql/conf:/etc/mysql/conf.d -v /data/mysql/data:/var/lib/mysql \
--env MYSQL_ROOT_PASSWORD=123456 \
--name mysqlcon -d --net host --rm mysql:latest

4.远程连接测试

## Nacos 单机版本的安装
本次安装的版本为Nacos2.0.2

1.拉取nacos镜像
docker pull nacos/nacos-server:2.0.2

2.运行镜像进行检测(仅仅检测看看有没有用) 这步骤可以略过 运行后记得停止容器
docker run -d -p 8848:8848 -p 9848:9848 -p 9849:9849 \
--rm \
-e MODE=standalone \
-e JVM_XMS=200m \
-e JVM_XMX=200m \
-e JVM_XMN=150m \
--name nacos nacos/nacos-server:2.0.2

3.如果没有idea连接docker,可以用下面的命名查看容器日志
docker logs [容器id]

4.创建nacos的数据库nacos并导入必要的DDL
文件放在ashore\docker\nacos\nacos_sql\nacos-mysql.sql

5.application.properties 这是nacos的配置文件
这里提供了参考properties文件放在\docker\nacos\nacos_appliaction\application.properties这个目录下
记得修改为自己的数据库地址
db.url.0=jdbc:mysql://x.x.x:3306/nacos?
db.user=xxx
db.password=xxx

6.创建容器
`下面两行的目录在linux下面提前建立好`
`/data/nacos_standalone/standalone-logs`
`/data/nacos_standalone/plugins`
`下面的这个是文件,把application.properties上传到服务器的这个conf文件夹内`
`/data/nacos_standalone/conf/application.properties`

docker run \
-p 8848:8848 -p 9848:9848 -p 9849:9849 \
-v /data/nacos_standalone/standalone-logs:/home/nacos/logs \
-v /data/nacos_standalone/plugins:/home/nacos/plugins \
-v /data/nacos_standalone/conf/application.properties:/home/nacos/conf/application.properties \
--env MODE=standalone --env JVM_XMS=150m --env JVM_XMX=150m --env JVM_XMN=140m \
--name nacos-server \
-d \
--net host \
--rm nacos/nacos-server:2.0.2

7.登录nacos可视化界面
ip:8848/nacos
默认账号 nacos
默认密码 nacos

8.添加一个配置,然后去数据库的config_info看有没有新增数据

## Redis安装
1.拉取最新的redis镜像
docker pull redis

2.服务器创建如下配置文件和文件夹用于挂载
/data/redis/conf/redis.conf
/data/redis/data

3.创建容器
docker run -p 6379:6379 \
-v /data/redis/conf/redis.conf:/etc/redis/redis.conf -v /data/redis/data:/data \
--name rediscon -d --privileged --net host --rm redis:latest \
redis-server /etc/redis/redis.conf --appendonly yes --requirepass 123456

`解释一下:
 --appendonly yes
这个命令是redis用于持久化,会创建一个aof文件用于存储
有些公司会使用rdb,例如游戏公司,为了性能的缘故可能就使用的rdb
--requirepass 123456 设置密码
`

## nginx安装
1.拉取最新的nginx镜像

2.服务器创建如下文件夹
/data/nginx/nginx.conf
/data/nginx/conf.d/default.conf --这个文件来修改映射路径和端口


3.创建容器
docker run -p 80:80 \
-v /data/nginx/nginx.conf:/etc/nginx/nginx.conf \
-v /data/nginx/conf.d/default.conf:/etc/nginx/conf.d/default.conf \
--name nginxcon -d --net host --rm nginx:latest 

## rabbitmq安装
1.拉取mq最新镜像
docker pull rabbitmq:management

2.创建文件用于持久化挂载
/data/rabbitmq

3.运行容器
docker run \
-p 5671:5671 -p 5672:5672 -p 4369:4369 -p 15671:15671 -p 15672:15672 -p 25672:25672 \
-v /data/rabbitmq:/var/lib/rabbitmq --name rabbitmq-server -d -i --net host --rm rabbitmq:management 

4.登录
http://192.168.80.128:15672/
默认账号密码都是 guest 

## 阶段实战部署root聚合项目下的gateway微服务和nacos微服务
0.准备工作,导入我准备好的nacos配置中心的配置 配置文件在
命名空间名称              命名空间ID          配置数     操作
distributed_test        distributed_test   2        详情 删除 编辑

1.先打包一个java8镜像,这是打包微服务镜像的基础
docker pull williamyeh/java8

2.打包一定要注意加spring-boot-maven-plugin插件到pom文件,这个很重要,如果不加,-jar启动时会找不到main类

3.编写dockerfile,dockerfile放在和target同一级目录,例如测试nacos微服务dockerfile详细信息  
FROM williamyeh/java8:latest                   # 依赖的java8镜像  
MAINTAINER itfeng<xxxxxx666@163.com>           # 作者信息  
COPY ./target/nacos.jar nacos.jar              # 复制targer下的nacos.jar 复制到 根目录  
ENTRYPOINT ["java","-jar","/nacos.jar"]        # 启动nacos测试微服务的的命令  

idea的运行配置里面有一个 [上传nacos微服务镜像] 运行配置 运行这个配置可以直接上传镜像到服务器  

4.启动nacos测试微服务容器  
docker run -p 8081:8081 --name nacosdemo -d --net host --rm nacos:23.4.7  

5.安按照上面同样的方法部署完gateway  

6.开始测试  
http://[服务器ip]:9992/api/admin/c  
页面可以得到获取的配置信息 test  

## 阶段实战部署root聚合项目下的shellpoj
1.编写dockerfile  
`这是我从公司直接copy的原版dockerfile内容,在shellpoj这个微服务下我自己重写了一个dockerfile,这里我把每行的意思详细解释一下  `  
`引入java8镜像  `    
FROM openjdk:8-jre                            
`作者信息`   
MAINTAINER tangxianglin<txline0420@163.com>  
`
在容器内创建2个目录app,logs  mkdir -p /shellpoj/{app,logs}    
软链接                     ln -sf  
修改容器的时区  echo "Asia/shanghai" > /etc/timezone   
`  
RUN mkdir -p /senergy/{app,logs} && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo "Asia/shanghai" > /etc/timezone;     
`工作目录 让我们进容器的时候默认目录就是这个`  
WORKDIR /senergy/app  
`将本机的jar 放到镜像内`  
ADD target/gzrb-estate.jar ./  
`暴露端口`  
EXPOSE 9003  
`一些启动参数`  
ENTRYPOINT ["java","-jar", "./gzrb-estate.jar","-XX:G1HeapRegionSize=16MB","-XX:-UseContainerSupport","-server","-XX:+UseStringDeduplication","-XX:+UseG1GC","-XX:+DisableExplicitGC", "-XX:-HeapDumpOnOutOfMemoryError","-XX:+AggressiveOpts"]  




2.在服务器做准备工作   
在服务器 /data/ 目录下建一个新目录 shellpoj  
在该目录下写几个shell 运行脚本  
脚本名 : start.sh  
脚本内容 :  
`#!/bin/bash  
docker run -d --name shellpoj-server \  
--restart always \  
--net=host \  
-v /data/shellpoj/logs:/shellpoj/logs \  
distributed/shellpoj:23.4.9`  

脚本名 : stop.sh  
脚本内容 :
`#!/bin/bash  
docker stop shellpoj-server  
`

脚本名 : remove.sh  
脚本内容 :  
`#!/bin/bash  
docker stop shellpoj-server &&  docker rm shellpoj-server  
`

脚本名 : delete.sh  
脚本内容 :  
`#!/bin/bash  
docker rm shellpoj-server  
`

脚本名 : logs.sh  
脚本内容 :  
`#!/bin/bash  
docker logs -f -t --tail 1000 shellpoj-server  
`

3.运行脚本  
sh start.sh  


4.测试功能  
然后可以开始测试shell命令和挂载日志  
进入 /data/shellpoj/logs 文件夹下面有了挂载的日志文件  

执行logs.sh    
可以看到滚动的日志  

执行stop.sh  
停止容器  

执行delete.sh  
删除容器  



