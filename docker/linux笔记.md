# linux笔记-centos7

#linux常用命令
systemctl restart network -重启网络
systemctl stop NetworkManager -查不到ip,可以用关闭网络管理

systemctl daemon-reload -重启docker
systemctl restart docker -重启docker
systemctl status docker  -查看状态

firewall-cmd --state --查看防火墙状态
systemctl stop firewalld.service --停止防火墙
systemctl disable firewalld.service --防止防火墙开机自启

– 开放指定端口
firewall-cmd --zone=public --add-port=1935/tcp --permanent
– 关闭指定端口
firewall-cmd --zone=public --remove-port=5672/tcp --permanent
– 重启防火墙
firewall-cmd --reload

mkdir -p a/a/a/ -创建多级目录


#### linux常见问题
centos7.3系统，已经关闭firewalld，但是除了22端口，其余端口无法被外界访问，本地访问正常，解决步骤：
1、先开启firewalld：systemctl start firewalld
2、放通端口：firewall-cmd --zone=public --add-port=8080/tcp --permanent
3、重新加载配置文件：firewall-cmd --reload
此时测试，端口已经能够访问了，如果不需要firewall，可以再关闭，已放通端口不受影响（为什么一开始我不放通端口直接关闭firewall不行？这点有了解的大神帮忙解答下）


第二种解决方法
iptables -vnL|grep 6379 --查看linux开放端口
/sbin/iptables -I INPUT -p tcp --dport 2375 -j ACCEPT -开放某个端口

/sbin/iptables -I INPUT -p tcp --dport 30000 -j ACCEPT


#### 常见问题
idea远程连接不上docker


#### 卸载非docker安装的mysql
执行如下命令查看已经安装的mysql
rpm -qa|grep -i mysql
执行如下命令卸载所有mysql软件包
yum -y remove mysql*


##### nginx
docker cp nginx:/etc/nginx/nginx.conf /data/nginx/ && docker cp nginx:/var/log/nginx /data/nginx/log && docker cp nginx:/etc/nginx/conf.d/default.conf /data/nginx/conf.d/

docker run --rm -d -v /data/nginx/nginx.conf:/etc/nginx/nginx.conf -v /data/nginx/conf.d/default.conf:/etc/nginx/conf.d/default.conf -p 80:80 --name=nginxtest nginx:latest

##### redis
docker run -p 6378:6379 -v /data/redis/conf/redis.conf:/usr/local/etc/redis/redis.conf --name rediscon -d --rm redis:latest redis-server /usr/local/etc/redis/redis.conf

##### mysql
docker run -d --rm -p 3306:3306 -v /data/mysql/conf:/etc/mysql/conf.d -v /data/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 --name  mysql mysql:latest


##### zookeeper
`--创建网络
app-tier：网络名称
–driver：网络类型为bridge`

docker network create app-tier --driver bridge

docker run -d --rm --name zookeeper-server \
--network app-tier \
-e ALLOW_ANONYMOUS_LOGIN=yes \
-p 2181:2181 \
zookeeper:latest

`可以查看日志`
docker logs -f zookeeper

`后面部署kafka会使用到zookeeper的ip地址`
docker inspect app-tier

进入zookeeper容器

[root@VM-4-9-centos ~]# docker exec -it zookeeper_kafka /bin/bash
root@032659ab5a02:/apache-zookeeper-3.7.0-bin# ls
LICENSE.txt  NOTICE.txt  README.md  README_packaging.md  bin  conf  docs  lib
root@032659ab5a02:/apache-zookeeper-3.7.0-bin# cd bin/
root@032659ab5a02:/apache-zookeeper-3.7.0-bin/bin# zkCli.sh

##### kafka

docker pull wurstmeister/kafka

##### 启动kafka
docker run -d --name kafka_zookeeper  --network zookeeper_network -p 9092:9092 -e KAFKA_BROKER_ID=0 -e KAFKA_ZOOKEEPER_CONNECT=<zookeeperIP地址>:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://<宿主机IP地址>:9092 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092  wurstmeister/kafka

##### kafka-map
docker pull kafka-map

`--rm 和 --restart always 冲突 自动重启容器 是不能删除容器的`
docker run -p 9666:8080 -v /data/kafka-map/data:/usr/local/kafka-map/data --env DEFAULT_USERNAME=admin --env DEFAULT_PASSWORD=admin --name kafka-map-UI -d --restart always --network app-tier dushixiang/kafka-map:latest 





