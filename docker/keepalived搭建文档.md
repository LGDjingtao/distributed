# keepalived简介
Linux下实现`轻量级`高可用的一种解决方案。  
Keepalived 的高可用通过虚拟ip（vip）来实现，虚拟 ip 可以漂移。 

# 环境
本次文档采用docker安装的方式。  
```txt
[root@senergy-01 tcpdump]# uname -a
Linux senergy-01 3.10.0-1160.99.1.el7.x86_64 #1 SMP Wed Sep 13 14:19:20 UTC 2023 x86_64 x86_64 x86_64 GNU/Linux
```
服务器A   
    默认网卡 p5p1  
    ip 192.168.1.148  

服务器B  
    默认网卡 enp2s0  
    ip 192.168.1.99  

# 前置工具
tcpdump抓包工具 下载    
```text
http://mirror.centos.org/centos/7/os/x86_64/Packages/
libpcap-1.5.3-12.el7.x86_64.rpm 
tcpdump-4.9.2-4.el7_7.1.x86_64.rpm
```

tcpdump抓包工具 安装
```shell
rpm -ivh libpcap-1.5.3-12.el7.x86_64.rpm
rpm -ivh tcpdump-4.9.2-4.el7_7.1.x86_64.rpm
```

# 获取镜像
```shell
docker pull osixia/keepalived:2.0.20
```

# 配置文件
主keepalived配置    
```text
global_defs {
  default_interface p5p1                                    
}

vrrp_instance VI_1 {
  interface p5p1

  state MASTER
  virtual_router_id 51
  priority 100
  nopreempt

  unicast_peer {
    192.168.1.148
    192.168.1.99
  }

  virtual_ipaddress {
    192.168.1.146
  }

  authentication {
    auth_type PASS
    auth_pass d0cker
  }

  notify "/container/service/keepalived/assets/notify.sh"
}
```

备keepalived配置  
```text
global_defs {
  default_interface enp2s0                                    
}

vrrp_instance VI_1 {
  interface enp2s0

  state MASTER
  virtual_router_id 51
  priority 80
  nopreempt

  unicast_peer {
    192.168.1.148
    192.168.1.99
  }

  virtual_ipaddress {
    192.168.1.146
  }

  authentication {
    auth_type PASS
    auth_pass d0cker
  }

  notify "/container/service/keepalived/assets/notify.sh"
}
```

## 抢占式和非抢占式 keepalived
配置`nopreempt`字段来标志这台keepalived服务是不是抢占式。  
A服务器(主keepalived)挂掉后 ip漂移到 B服务器(备keepalived)  
抢占式：  
    A服务器重启后，检测到A服务器的keepalived起来后，A服务器的keepalived会抢占ip，ip会漂移回A服务器。
非抢占式：  
    A服务器重启后，ip还是在B服务器。

# 启动服务
启动前可以用
```shell
ip a
```
查看网卡详情

A服务器启动keepalived  
```shell
docker run -d \
--name keepalived-master \
-v /home/keepalived/conf/keepalived.conf:/container/service/keepalived/assets/keepalived.conf \
--net=host \
--privileged=true \
osixia/keepalived:2.0.20 --loglevel debug --copy-service
```

B服务器启动keepalived
```shell
docker run -d \
--name keepalived-backup \
-v  /home/keepalived/conf/keepalived.conf:/container/service/keepalived/assets/keepalived.conf \
--net=host \
--privileged=true \
osixia/keepalived:2.0.20 --loglevel debug --copy-service
```



# 检查vip
```shell
ip a
```

```shell
2: p5p1: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc pfifo_fast state UP group default qlen 1000
    link/ether xxx brd xxx
    inet 192.168.1.148/24 brd 192.168.1.255 scope global dynamic enp2s0
       valid_lft 158673sec preferred_lft 158673sec
    inet 192.168.1.146/32 scope global enp2s0
       valid_lft forever preferred_lft forever
    inet6 xxxx
       valid_lft xxx
    inet6 xxx
```

发现p5p1网卡绑定了2个ipv4
192.168.1.148 为物理机地址  
192.168.1.146 为vip

A服务器抓包查看
```shell
tcpdump -nnvvS src 192.168.1.148 -i p5p1 -c 10 and vrrp
```


# 测试ip漂移
可以关闭服务器A的keepalived，去服务器B输入
```shell
ip a
```
查看网卡,发现漂移到了192.168.1.99
```shell
2: enp2s0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc pfifo_fast state UP group default qlen 1000
    link/ether a4:ae:12:f3:7e:aa brd ff:ff:ff:ff:ff:ff
    inet 192.168.1.99/24 brd 192.168.1.255 scope global dynamic enp2s0
       valid_lft 158673sec preferred_lft 158673sec
    inet 192.168.1.146/32 scope global enp2s0
       valid_lft forever preferred_lft forever
    inet6 240e:3b4:aaa:a2c0:a6ae:12ff:fef3:7eaa/64 scope global mngtmpaddr dynamic 
       valid_lft 259027sec preferred_lft 172627sec
    inet6 fe80::a6ae:12ff:fef3:7eaa/64 scope link 
```
# 配置文件详解
```text
global_defs {                                     #全局定义部分
    notification_email {                          #设置报警邮件地址，可设置多个
        acassen@firewall.loc                      #接收通知的邮件地址
    }
    notification_email_from test0@163.com         #设置 发送邮件通知的地址
    smtp_server smtp.163.com                      #设置 smtp server 地址，可是ip或域名.可选端口号 （默认25）
    smtp_connect_timeout 30                       #设置 连接 smtp server的超时时间
    router_id LVS_DEVEL                           #主机标识，用于邮件通知
    vrrp_skip_check_adv_addr
    vrrp_strict                                   #严格执行VRRP协议规范，此模式不支持节点单播
    vrrp_garp_interval 0
    vrrp_gna_interval 0
    script_user keepalived_script                 #指定运行脚本的用户名和组。默认使用用户的默认组。如未指定，默认为keepalived_script 用户，如无此用户，则使用root
    enable_script_security                        #如果路径为非root可写，不要配置脚本为root用户执行。
}

vrrp_script chk_nginx_service {                   #VRRP 脚本声明
    script "/etc/keepalived/chk_nginx.sh"         #周期性执行的脚本
    interval 3                                    #运行脚本的间隔时间，秒
    weight -20                                    #权重，priority值减去此值要小于备服务的priority值
    fall 3                                        #检测几次失败才为失败，整数
    rise 2                                        #检测几次状态为正常的，才确认正常，整数
    user keepalived_script                        #执行脚本的用户或组
}

vrrp_instance VI_1 {                              #vrrp 实例部分定义，VI_1自定义名称
    state MASTER                                  #指定 keepalived 的角色，必须大写 可选值：MASTER|BACKUP
    interface ens33                               #网卡设置，lvs需要绑定在网卡上，realserver绑定在回环口。区别：lvs对访问为外，realserver为内不易暴露本机信息
    virtual_router_id 51                          #虚拟路由标识，是一个数字，同一个vrrp 实例使用唯一的标识，MASTER和BACKUP 的 同一个 vrrp_instance 下 这个标识必须保持一致
    priority 100                                  #定义优先级，数字越大，优先级越高。
    advert_int 1                                  #设定 MASTER 与 BACKUP 负载均衡之间同步检查的时间间隔，单位为秒，两个节点设置必须一样
    authentication {                              #设置验证类型和密码，两个节点必须一致
        auth_type PASS
        auth_pass 1111
    }
    virtual_ipaddress {                           #设置虚拟IP地址，可以设置多个虚拟IP地址，每行一个
        192.168.119.130
    }
    track_script {                                #脚本监控状态
        chk_nginx_service                         #可加权重，但会覆盖声明的脚本权重值。chk_nginx_service weight -20
    }
    notify_master "/etc/keepalived/start_haproxy.sh start"  #当前节点成为master时，通知脚本执行任务
    notify_backup "/etc/keepalived/start_haproxy.sh stop"   #当前节点成为backup时，通知脚本执行任务
    notify_fault  "/etc/keepalived/start_haproxy.sh stop"   #当当前节点出现故障，执行的任务;
}

## 虚拟服务器virtual_server定义块 ，虚拟服务器定义是keepalived框架最重要的项目了，是keepalived.conf必不可少的部分。 该部分是用来管理LVS的，是实现keepalive和LVS相结合的模块。
virtual_server 192.168.119.130 80  {          #定义RealServer对应的VIP及服务端口，IP和端口之间用空格隔开
    delay_loop 6                              #每隔6秒查询realserver状态
    lb_algo rr                                #后端调试算法（load balancing algorithm）
    lb_kind DR                                #LVS调度类型NAT/DR/TUN
    #persistence_timeout 60                   同一IP的连接60秒内被分配到同一台realserver
    protocol TCP                              #用TCP协议检查realserver状态
    real_server 192.168.119.120 80 {
        weight 1                              #权重，最大越高，lvs就越优先访问
        TCP_CHECK {                           #keepalived的健康检查方式HTTP_GET | SSL_GET | TCP_CHECK | SMTP_CHECK | MISC
            connect_timeout 10                #10秒无响应超时
            retry 3                           #重连次数3次
            delay_before_retry 3              #重连间隔时间
            connect_port 80                   #健康检查realserver的端口
        }
    }
    real_server 192.168.119.121 80 {
        weight 1                              #权重，最大越高，lvs就越优先访问
        TCP_CHECK {                           #keepalived的健康检查方式HTTP_GET | SSL_GET | TCP_CHECK | SMTP_CHECK | MISC
            connect_timeout 10                #10秒无响应超时
            retry 3                           #重连次数3次
            delay_before_retry 3              #重连间隔时间
            connect_port 80                   #健康检查realserver的端口
        }
    }
}
```

# 进阶
## 配置文件-vrrp_script区域
用来做健康检查的，当时检查失败时会将vrrp_instance的priority减少相应的值。
```text
vrrp_script chk_http_port {   
    script "</dev/tcp/127.0.0.1/80"       #一句指令或者一个脚本文件，需返回0(成功)或非0(失败)，keepalived以此为依据判断其监控的服务状态。
    interval 1    #健康检查周期
    weight -10   # 优先级变化幅度，如果script中的指令执行失败，那么相应的vrrp_instance的优先级会减少10个点。
}
```

## HAPorxy健康检查文件
由于HAPorxy交给了swarm管理，暂时不做HAPorxy的健康检查

# 使用stack部署

```shell
version: "3.8"
services:
    keepalived1:
      image: 192.168.1.240:5000/osixia/keepalived:2.0.20
      volumes:
        - /home/keepalived/conf/keepalived.conf:/container/service/keepalived/assets/keepalived.conf
      networks:
        hostnet: {}
      cap_add:
        - NET_ADMIN
        - NET_BROADCAST
        - NET_RAW
      # privileged: true
      command:  --loglevel debug  --copy-service  
      deploy:
        placement:
          constraints:
            - node.hostname == senergy-01
            - node.labels.role == keepalived1
    keepalived2:
      image: 192.168.1.240:5000/osixia/keepalived:2.0.20
      volumes:
        - /home/keepalived/conf/keepalived.conf:/container/service/keepalived/assets/keepalived.conf
      networks:
        hostnet: {}
      cap_add:
        - NET_ADMIN
        - NET_BROADCAST
        - NET_RAW
      # privileged: true
      command:  --loglevel debug  --copy-service
      deploy:
        placement:
          constraints:
            - node.hostname == senergy-03
            - node.labels.role == keepalived2
networks:
  hostnet:
    external: true
    name: host
```
privileged 在stack中会被忽略，由于keepalive需要很高的网络权限，所以网络权限必不可少
```shell
      cap_add:
        - NET_ADMIN
        - NET_BROADCAST
        - NET_RAW
```
启动命令一定要给   --copy-service
不然不能copy .conf文件导致服务起不来