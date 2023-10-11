# docker搭建nfs流程

## 搭建nfs服务端

### 1.获取nfs镜像
docker pull fuzzle/docker-nfs-server

### 2.启动容器
`
docker run -d --privileged  \
-v /home/docker/nfs01:/nfs \
-e NFS_EXPORT_DIR_1=/nfs \
-e NFS_EXPORT_DOMAIN_1=\* \
-e NFS_EXPORT_OPTIONS_1=rw,insecure,no_subtree_check,no_root_squash,fsid=1 \
-p 111:111 -p 111:111/udp \
-p 2049:2049 -p 2049:2049/udp \
-p 32765:32765 -p 32765:32765/udp \
-p 32766:32766 -p 32766:32766/udp \
-p 32767:32767 -p 32767:32767/udp \
fuzzle/docker-nfs-server:latest
`
### 3.可能会遇到的问题
nfs容器会映射111端口 可能会和本机的rpcbind进程冲突  
看情况，这个需要杀掉宿主机的rpcbind进程  
netstat -tanlp  
kill 【rpcbind的pid】  

### 4.检测nfs服务器的共享文件夹
showmount -e  [服务端ip]  
一般会返回如下  
Export list for 192.168.29.129:
/nfs *

### 5.安装nfs客户端
yum install -y nfs-utils  

### 6.本地挂载到nfs的共享目录
mount -t nfs 192.168.1.107:/nfs   [本地服务器的某个目录] 

## 取消挂载步骤

### 1.查看共享目录被什么进程占用
fuser -m [客户端目录]

可以一个一个杀死使用词目录的进程，也可以使用如下命令全部kill
fuser -k [客户端目录]

### 2.解除挂载
umount [客户端目录] 



