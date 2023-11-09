
# 作用
hostname 和 label  可以让swarm识别是那一台主机和某一种类型的node  
从而可以实现精准部署不同服务到指定的hostname和label下面

# hostname
## 查看hostname
```shell
[root@senergy-01 etc]# docker node ls
ID                            HOSTNAME     STATUS    AVAILABILITY   MANAGER STATUS   ENGINE VERSION
uaumitu8o6svgllriau5lgqxh *   192.168.1.148   Ready     Drain          Leader           24.0.6
rv9uz8f1a482qm1kcok20yzbz     senergy-02   Ready     Active         Reachable        24.0.6
ags1dgokv3ta86fxhveo8gl2e     senergy-03   Ready     Active         Reachable        24.0.6
```

## 修改hostname
在192.168.1.148执行
```shell
hostnamectl set-hostname senergy-01  
service docker restart
```
就可以改192.168.1.148 为 senergy-01  
```shell
[root@senergy-01 etc]# docker node ls
ID                            HOSTNAME     STATUS    AVAILABILITY   MANAGER STATUS   ENGINE VERSION
uaumitu8o6svgllriau5lgqxh *   senergy-01   Ready     Drain          Leader           24.0.6
rv9uz8f1a482qm1kcok20yzbz     senergy-02   Ready     Active         Reachable        24.0.6
ags1dgokv3ta86fxhveo8gl2e     senergy-03   Ready     Active         Reachable        24.0.6
```

# label
## 查看label
```shell
docker node inspect --pretty senergy-01
```

```shell
ID:			uaumitu8o6svgllriau5lgqxh
Labels:
 - consul=leader
Hostname:              	senergy-01
Joined at:             	2023-10-19 09:29:32.471909854 +0000 utc
Status:
 State:			Ready
 Availability:         	Drain
 Address:		192.168.1.148
Manager Status:
 Address:		192.168.1.148:2377
 Raft Status:		Reachable
 Leader:		Yes
    
 ...skip
```

## 添加label
添加人工label.role  = keepalive1
```shell
docker node update --label-add role=keepalive1 senergy-01
docker node inspect --pretty senergy-01
```
```shell
Labels:
 - consul=leader
 - role=keepalive1
Hostname:              	senergy-01
Joined at:             	2023-10-19 09:29:32.471909854 +0000 utc
Status:
 State:			Ready
 Availability:         	Drain
 Address:		192.168.1.148
Manager Status:
 Address:		192.168.1.148:2377
 Raft Status:		Reachable
 Leader:		Yes
```
