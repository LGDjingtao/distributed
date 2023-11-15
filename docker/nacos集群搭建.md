```yaml
    nacos1:
      image: 192.168.1.240:5000/nacos/nacos-server:v2.1.0
      volumes:
        - /home/data/nacos-data/logs:/home/nacos/logs
        - /usr/share/zoneinfo/Asia/Shanghai:/usr/share/zoneinfo/Asia/Shanghai
     # ports:
      #  - 8848:8848
      #  - 9848:9848
      #  - 9849:9849
      networks:
        hostnet: {}
      environment:
        - TZ=Asia/Shanghai
        - PREFER_HOST_MODE=hostname
        - MODE=cluster
        - NACOS_APPLICATION_PORT=8848
        - NACOS_SERVERS=192.168.1.148:8848 192.168.1.99:8848
        - SPRING_DATASOURCE_PLATFORM=mysql
        - MYSQL_SERVICE_HOST=192.168.1.148
        - MYSQL_SERVICE_PORT=3306
        - MYSQL_SERVICE_DB_NAME=nacos_config
        - MYSQL_SERVICE_USER=root
        - MYSQL_SERVICE_PASSWORD=user*2023
        - NACOS_SERVER_IP=192.168.1.148
        - MYSQL_SERVICE_DB_PARAM=characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true
      healthcheck:
        test: ["CMD-SHELL", "curl -sS 192.168.1.148:8848/nacos || exit 1"]
        interval: 10s
        timeout: 5s
        retries: 30
      depends_on:
        mysql-master:
          condition: service_healthy
        mysql-slave:
          condition: service_healthy
      deploy:
        endpoint_mode: dnsrr
        placement:
          constraints:
            - node.hostname == senergy-01
        mode: global
    nacos2:
      image: 192.168.1.240:5000/nacos/nacos-server:v2.1.0
      volumes:
        - /home/data/nacos-data/logs:/home/nacos/logs
        - /usr/share/zoneinfo/Asia/Shanghai:/usr/share/zoneinfo/Asia/Shanghai
     # ports:
      #  - 8848:8848
      #  - 9848:9848
      #  - 9849:9849
      networks:
        hostnet: {}
      environment:
        - TZ=Asia/Shanghai
        - PREFER_HOST_MODE=hostname
        - MODE=cluster
        - NACOS_APPLICATION_PORT=8848
        - NACOS_SERVERS=192.168.1.148:8848 192.168.1.99:8848
        - SPRING_DATASOURCE_PLATFORM=mysql
        - MYSQL_SERVICE_HOST=192.168.1.148
        - MYSQL_SERVICE_PORT=3306
        - MYSQL_SERVICE_DB_NAME=nacos_config
        - MYSQL_SERVICE_USER=root
        - MYSQL_SERVICE_PASSWORD=user*2023
        - NACOS_SERVER_IP=192.168.1.99
        - MYSQL_SERVICE_DB_PARAM=characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true
      healthcheck:
        test: ["CMD-SHELL", "curl -sS 192.168.1.99:8848/nacos || exit 1"]
        interval: 10s
        timeout: 5s
        retries: 30
      depends_on:
        mysql-master:
          condition: service_healthy
        mysql-slave:
          condition: service_healthy
      deploy:
        endpoint_mode: dnsrr
        placement:
          constraints:
            - node.hostname == senergy-03
        mode: global
```