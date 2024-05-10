限制docker-compose和docker中日志文件
docker-compose

1  进入docker-compose.yaml中，

         image: nginx:1.12.1

         restart: always

         logging:
            driver: "json-file"   #默认的文件日志驱动
            options:
             max-size: "500m"
             max-file: "3"



有兴趣的可以自己去官网研究。不启用日志的话  驱动那栏driver: "none"就可以了

max-size: "500m" 单个文件大小为500m   单位好像是忽视大小的  官网用的都是小写的k ，m，g

max-file: "10" # 最多10个文件

syslog 远程日志驱动程序下，可以使用 syslog-address 指定日志接收地址。

logging:

driver: syslog

options:

      syslog-address: "tcp://192.168.0.1:22"



2、重启docker守护进程
systemctl daemon-reload && systemctl restart docker



docker配置

1、新建 daemon.json
vi /etc/docker/daemon.json



max-size=500m，意味着一个容器日志大小上限是500M，
max-file=3，意味着一个容器有三个日志，分别是id+.json、id+1.json、id+2.json。

{

"log-driver":"json-file",
"log-opts": {"max-size":"500m", "max-file":"3"}
}



2、重启docker守护进程
systemctl daemon-reload && systemctl restart docker