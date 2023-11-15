# 镜像

使用bitnami的mysql8.2镜像

# 编排

```yaml
    mysql-master:
      image: docker.io/bitnami/mysql:8.2
      environment:
        - MYSQL_REPLICATION_MODE=master
        - MYSQL_REPLICATION_USER=repl_user
        - MYSQL_DATABASE=my_database
        - ALLOW_EMPTY_PASSWORD=yes
        - MYSQL_ROOT_PASSWORD=user*2023
        - MYSQL_MASTER_HOST=192.168.1.99
        - MYSQL_MASTER_PORT_NUMBER=3306
        - MYSQL_MASTER_ROOT_PASSWORD=user*2023
      volumes:
        - '/home/mysql_master_data:/bitnami/mysql/data'
      networks:
        hostnet: { }
      healthcheck:
        test: [ 'CMD', '/opt/bitnami/scripts/mysql/healthcheck.sh' ]
        interval: 15s
        timeout: 5s
        retries: 6
      deploy:
        endpoint_mode: dnsrr
        placement:
          constraints:
            - node.hostname == senergy-01
        mode: global
    mysql-slave:
      image: docker.io/bitnami/mysql:8.2
      environment:
        - MYSQL_REPLICATION_MODE=master
        - MYSQL_REPLICATION_USER=repl_user
        - MYSQL_DATABASE=my_database
        - ALLOW_EMPTY_PASSWORD=yes
        - MYSQL_ROOT_PASSWORD=user*2023
        - MYSQL_MASTER_HOST=192.168.1.148
        - MYSQL_MASTER_PORT_NUMBER=3306
        - MYSQL_MASTER_ROOT_PASSWORD=user*2023
      volumes:
        - '/home/mysql_master_data:/bitnami/mysql/data'
      networks:
        hostnet: { }
      healthcheck:
        test: [ 'CMD', '/opt/bitnami/scripts/mysql/healthcheck.sh' ]
        interval: 15s
        timeout: 5s
        retries: 6
      deploy:
        endpoint_mode: dnsrr
        placement:
          constraints:
            - node.hostname == senergy-03
        mode: global
```

# 数据同步设置

注意：两边容器都需要配置同步
1. 先看主机A的状态
```shell
SHOW MASTER STATUS;
```

2. 通过A主机的状态修改MASTER_LOG_FILE 和  MASTER_LOG_POS
```shell
CREATE USER 'slave'@'%' IDENTIFIED WITH mysql_native_password BY 'user*2023';
#分配权限
GRANT REPLICATION SLAVE ON *.* TO 'slave'@'%';
#刷新权限
flush privileges;  

CHANGE MASTER TO 
MASTER_HOST='192.168.1.148',
MASTER_USER='slave',
MASTER_PASSWORD='user*2023',
MASTER_PORT=3306,
MASTER_LOG_FILE='mysql-bin.000003',
MASTER_LOG_POS=821;

start slave;
show slave status\G;
```

3.验证是否配置成功过  
Slave_IO_Running : Yes  
Slave_SQL_Running : Yes  
这两个字段都是Yes就是配置成功