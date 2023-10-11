# linux笔记-centos7

# linux常用命令
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

上传文件到服务器
scp -r  ./sn-iot.jar  root@192.168.1.158:/home/build-image/security

使用netcat上传
服务器A开启监听  
nc -l  [port]  >  [文件名可随意填]  
服务器B发送数据
nc [ip] [port] < [要发送给的文件]

删除多个文件和目录 
rm -rf ./xx  xx  xx xx
记住一定加./

mv也要注意迁移目录大小和路径

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


#### idea常见问题
idea远程连接不上docker

#### docker常见问题
docker怎么进入容器
sudo docker exec -it [容器code] /bin/bash

#### 卸载非docker安装的mysql
执行如下命令查看已经安装的mysql
rpm -qa|grep -i mysql
执行如下命令卸载所有mysql软件包
yum -y remove mysql*

### 使用netcat开放系统后门


