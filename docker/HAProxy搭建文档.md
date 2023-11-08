# HAProxy简介
专注于做负载均衡的软件

# 环境
本次文档采用docker安装的方式。
```txt
[root@senergy-01 tcpdump]# uname -a
Linux senergy-01 3.10.0-1160.99.1.el7.x86_64 #1 SMP Wed Sep 13 14:19:20 UTC 2023 x86_64 x86_64 x86_64 GNU/Linux
```

# 获取镜像
```shell
docker pull haproxy
```

# 配置文件
新建配置文件haproxy.cfg
```shell
defaults
    mode            tcp
    log             global
    option          tcplog
    option          dontlognull
    option http-server-close
    option          redispatch
    retries         3
    timeout http-request 10s
    timeout queue   1m
    timeout connect 10s
    timeout client  1m
    timeout server  1m
    timeout http-keep-alive 10s
    timeout check   10s
    maxconn         3000
frontend    nacos
    bind        0.0.0.0:13307
    mode        tcp
    log         global
    default_backend nacos_server

backend     nacos_server
    balance roundrobin
    option httpchk
    http-check send meth HEAD uri /nacos
    server nacos1 192.168.1.148:8848 check inter 5s rise 2 fall 3
    server nacos2 192.168.1.99:8848 check inter 5s rise 2 fall 3

listen stats
    mode    http
    bind    0.0.0.0:1080
    stats   enable
    stats   hide-version
    stats uri /haproxyamdin?stats
    stats realm Haproxy\ Statistics
    stats auth admin:admin
    stats admin if TRUE
```
## 健康检查
```shell
    option httpchk
    http-check send meth HEAD uri /nacos
```
这里配置了http健康检查  默认情况下，运行状况检查探测将接受状态代码在 2xx 或 3xx 范围内的任何服务器响应为成功。

# 启动服务
```shell
docker run \
-p 1080:1080 \
-p 13307:13307 \
-d \
--name haproxy-master \
-v /home/haproxy-master/haproxy.cfg:/usr/local/etc/haproxy/haproxy.cfg \
--privileged=true haproxy
```


# 配置文件详解
```shell
global                                                 # 全局参数global模块的设置
    log         127.0.0.1 local2                      # log语法：log <address_1>[max_level_1] # 全局的日志配置，使用log关键字，指定使用127.0.0.1上的syslog服务中的local0日志设备，记录日志等级为info的日志
    chroot      /var/lib/haproxy              #工作目录
    pidfile     /var/run/haproxy.pid          #进程pid文件
    maxconn     4000                          #最大连接数
    user        haproxy                       #所属用户
    group       haproxy                       #所属用户组
    daemon                                    #以守护进程方式运行haproxy
stats socket /var/lib/haproxy/stats       #定义socket套接字，针对在线维护很有帮助
 
defaults                                      # defaults模块的设置
    mode                    http              #默认的模式{ tcp|http|health},health只会返回OK
    log                     global            #应用全局的日志配置
    option                  httplog           #启用日志记录HTTP请求，默认不记录HTTP请求日志                                                                
    option                 dontlognull        # 启用该项，日志中将不会记录空连接。所谓空连接就是在上游的负载均衡器者监控系统为了探测该 服务是否存活可用时，需要定期的连接或者获取某一固定的组件或页面，或者探测扫描端口是否在监听或开放等动作被称为空连接；官方文档中标注，如果该服务上游没有其他的负载均衡器的话，建议不要使用该参数，因为互联网上的恶意扫描或其他动作就不会被记录下来
    option http-server-close                  #每次请求完毕后主动关闭http通道
    option forwardfor       except 127.0.0.0/8   #如果服务器上的应用程序想记录发起请求的客户端的IP地址，需要在HAProxy上配置此选项， 这样 HAProxy会把客户端的IP信息发送给服务器，在HTTP                                                                            请求中添加"X-Forwarded-For"字段。 启用  X-Forwarded-For，在requests                                                                            头部插入客户端IP发送给后端的server，使后端server获取到客户端的真实IP。 
    option                  redispatch       # 当使用了cookie时，haproxy将会将其请求的后端服务器的serverID插入到cookie中，以保证会话的SESSION持久性；而此时，如果后端的服务器宕掉                                                                            了， 但是客户端的cookie是不会刷新的，如果设置此参数，将会将客户的请                                                                            求强制定向到另外一个后端server上，以保证服务的正常。
    retries                 3              # 定义连接后端服务器的失败重连次数，连接失败次数超过此值后将会将对应后端服务器标记为不可用
    timeout http-request    10s             #http请求超时时间
    timeout queue           1m              #一个请求在队列里的超时时间
    timeout connect         10s             #连接超时
    timeout client          1m              #客户端超时
    timeout server          1m              #服务器端超时
    timeout http-keep-alive 10s             #设置http-keep-alive的超时时间
    timeout check           10s             #检测超时
    maxconn                 3000            #每个进程可用的最大连接数
 
listen stats                                #定义一个listen模块，用于状态检测
    mode http                               #模式采用http
    bind 0.0.0.0:8888                       #绑定本机的地址及端口
    stats enable                            #启用状态检测功能
    stats uri     /haproxy-status           #状态检测的URI
    stats auth    haproxy:123456            #访问检测界面的用户名和密码
 
frontend  main *:80                         #frontend模块的设置，定义了一个前端
    acl url_static       path_beg       -i /static /images /javascript /stylesheets
    acl url_static       path_end       -i .jpg .gif .png .css .js      #这里定义了一个acl规则
    use_backend static   if  url_static     #如果匹配到了acl，则访问后端的static模块
    default_backend             my_webserver #如果没有匹配到acl，则将请求丢给默认的模块
 
backend static                          #定义第一个后端模块，static
    balance     roundrobin              #负载均衡算法为轮询
    server      static 127.0.0.1:80 check         #后端服务器地址
 
backend my_webserver                    #定第二个后端，my_wenserver
    balance     roundrobin              #负载均衡算法
    server  web01 172.31.2.33:80  check inter 2000 fall 3 weight 30              #定义的多个后端
    server  web02 172.31.2.34:80  check inter 2000 fall 3 weight 30              #定义的多个后端
    server  web03 172.31.2.35:80  check inter 2000 fall 3 weight 30              #定义的多个后端
```

