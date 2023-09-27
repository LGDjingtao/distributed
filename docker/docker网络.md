# 单体开发模式docker容器的4种网络模式

## 4种网络容器概览
![img.png](img%2Fimg.png)

## Close容器
`
意味着网络容器不允许任何的网络流量  
容器种的进程只能访问本地对的回环接口 
`
`
所以进程只需要和本身或者和其他本地进程通信  
选择这一种非常合适
`
### Close容器创建
`
docker run --rm \
--net none \
alpine:latest \
ip addr
`

## 

