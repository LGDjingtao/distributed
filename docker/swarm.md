# 安装好docker

yum search docker
yum install -y docker
systemctl start docker
systemctl status docker

# 加入开机自启

systemctl enable docker

# 更改镜像源

vi /etc/docker/daemon.json
{
"registry-mirrors": ["http://hub-mirror.c.163.com"]
}
或
{
"registry-mirrors": ["https://docker.mirrors.ustc.edu.cn"]
}
# 重 启
systemctl restart docker.service

# 关 闭SELinux 

[root@swaram01 ~]# getenforce
Disabled
[root@swaram01 ~]# /usr/sbin/sestatus -v
SELinux status:                 disabled

vi /etc/selinux/config
将SELINUX=enforcing改为SELINUX=disabled 

# 编辑hosts文件
vi /etc/hosts  
192.168.182.110 swarm01  
192.168.182.111 swarm02  

# 关闭防火墙
systemctl stop firewalld.service #停止firewall
systemctl disable firewalld.service #禁止firewall开机启动

# 修改监听端口
vim  /lib/systemd/system/docker.service  

-H tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock

# 重启docker服务
systemctl daemon-reload    ##使配置文件生效
systemctl restart docker  


# 安装swarm
docker pull swarm  

# manager上初始化swarm
docker swarm init --advertise-addr  【manager主机ip】

# 添加集群节点
SWMTKN 和 后面的ip都是从manager拿的
docker swarm join --token SWMTKN-1-55074oer8dieyfkpistpv7qy9ig0a0rczopkqq7cwg9y8ob3ot-3zg990p4hdr93k1l2j6uhts93 192.168.29.128:2377
