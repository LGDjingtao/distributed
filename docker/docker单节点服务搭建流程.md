# 单节点搭建流程

## 1.私有仓库
### 1.1 私有搭建
使用docker官方的私有仓库【registry】。  
先准备好私有仓库的配置文件，由于默认配置文件删除镜像很麻烦，这里提供好了配置文件。
```yaml
version: 0.1
log:
  fields:
    service: registry
storage:
  cache:
    blobdescriptor: inmemory
  filesystem:
    rootdirectory: /var/lib/registry
  delete:
    enabled: true
http:
  addr: :5000
  headers:
    X-Content-Type-Options: [nosniff]
health:
  storagedriver:
    enabled: true
    interval: 10s
    threshold: 3
```
【registry】 启动脚本
```shell
docker run -d --net=host \
--name=registry \
--restart=always \
--privileged=true \
-v /home/senergy/airport-data/registry-data/registry:/var/lib/registry \
-v /home/senergy/airport-data/registry-data/config/config.yml:/etc/docker/registry/config.yml \
registry:latest
```
### 1.2私仓使用

#### 1.2.1 地址配置
先配置仓库地址  
在/etc/docker/daemon.json中配置  
```json
{
    "insecure-registries": ["【ip】:5000"]
}
```
加载私仓并重启docker服务
```shell
systemctl daemon-reload
systemctl restart docker.service
```

可能会出现的问题-重启docker服务失败  
可能原因有2种：  
1.docker版本过低，需要在docker启动文件配置私有仓库地址
```shell
sudo vim /usr/lib/systemd/system/docker.service
```
在启动文件中配置私仓地址  `ExecStart=` 加上  `--insecure-registry=【ip】:5000`
```properties
ExecStart=/usr/bin/dockerd -H fd:// --containerd=/run/containerd/containerd.sock --insecure-registry=【ip】:5000
```
2.配置多个私仓冲突  
删除之前的私仓，再重启

#### 1.2.2 镜像推送和拉取
推送镜像的格式有要求: `[ip]:[port]/[image_name]:[tag]`  
所以要推送镜像到私有仓库需要先`docker tag`修改镜像格式
```shell
docker tag nacos/nacos-server:v2.1.0 192.168.1.240:5000/nacos/nacos-server:v2.1.0
```
推送到私仓
```shell
docker push 192.168.1.240:5000/nacos/nacos-server:v2.1.0
```

拉取镜像
```shell
docker pull 192.168.1.240:5000/nacos/nacos-server:v2.1.0
```

#### 1.2.2 私有仓库的管理
由于官方的私有仓库【registry】 不具备管理能力，需要第3方的管理工具赋能。  
管理工具安装启动脚本：  
```shell
docker run -p 8280:80 --name registry-ui \
-e REGISTRY_URL="http://【私有仓库ip】:5000" \
-e DELETE_IMAGES="true" \
-e REGISTRY_TITLE="Registry2" \
-d joxit/docker-registry-ui:1.5-static
```
`
浏览器打开【安装服务器ip】:【8280】
`

### 2.服务搭建
mysql redis minio mqtt naocs的compose脚本
```yaml
version: "3.8"
services:
  mysql:
    image: 192.168.1.240:5000/mysql:8.0.31
    container_name: mysql
    restart: always
    networks:
      - by-network
    ports:
      - "3306:3306"
    environment:
      - TZ=Asia/Shanghai
      - MYSQL_ROOT_PASSWORD=user*2023
    volumes:
      - /home/data/mysql-data/conf:/etc/mysql/conf.d
      - /home/data/mysql-data/data:/var/lib/mysql
      - /usr/share/zoneinfo/Asia/Shanghai:/usr/share/zoneinfo/Asia/Shanghai

  redis:
    image: 192.168.1.240:5000/redis:6.2.8
    container_name: redis
    restart: always
    networks:
      - by-network
    ports:
      - "6379:6379"
    volumes:
      - /home/data/redis-data/data:/data
      - /home/data/redis-data/redis.conf:/etc/redis/redis.conf
    # 以配置文件的方式启动 redis.conf
    command: redis-server /etc/redis/redis.conf

  emqx:
    image: 192.168.1.240:5000/emqx/emqx:4.3.10
    container_name: emqx
    restart: always
    networks:
      - by-network
    ports:
      - "1883:1883"
      - "8083:8083"
      - "8084:8084"
      - "8883:8883"
      - "18083:18083"

  minio:
    image: 192.168.1.240:5000/minio/minio:23.1.6
    container_name: minio
    restart: always
    networks:
      - by-network
    ports:
      - "9000:9000"
      - "9095:9095"
    environment:
      - TZ=Asia/Shanghai
      - MINIO_ACCESS_KEY=admin
      - MINIO_SECRET_KEY=senergy123
      - MINIO_ACCESS_KEY_FILE=access_key
      - MINIO_SECRET_KEY_FILE=secret_key
      - MINIO_ROOT_USER_FILE=access_key
      - MINIO_ROOT_PASSWORD_FILE=secret_key
      - MINIO_KMS_MASTER_KEY_FILE=kms_master_key
      - MINIO_SSE_MASTER_KEY_FILE=sse_master_key
      - MINIO_UPDATE_MINISIGN_PUBKEY=RWTx5Zr1tiHQLwG9keckT0c45M3AGeHD6IvimQHpyRywVWGbP1aVSGav
    volumes:
      - /home/data/minio-data/data:/data
      - /home/data/minio-data/config:/root/.minio
      - /usr/share/zoneinfo/Asia/Shanghai:/usr/share/zoneinfo/Asia/Shanghai
    command: server /data --console-address ":9095" -address ":9000"

  nacos:
    image: 192.168.1.240:5000/nacos/nacos-server:v2.1.0
    container_name: nacos
    restart: always
    networks:
      - by-network
    ports:
      - "8848:8848"
      - "9848:9848"
    environment:
      - TZ=Asia/Shanghai
      - MODE=standalone
      - MYSQL_DATABASE_NUM=1
      - SPRING_DATASOURCE_PLATFORM=mysql
      - MYSQL_SERVICE_HOST=mysql
      - MYSQL_SERVICE_PORT=3306
      - MYSQL_SERVICE_DB_NAME=nacos_config
      - MYSQL_SERVICE_USER=root
      - MYSQL_SERVICE_PASSWORD=user*2023
      - MYSQL_SERVICE_DB_PARAM=characterEncoding=utf8&connectTimeout=10000&socketTimeout=3000&autoReconnect=true&useSSL=false&serverTimezone=UTC
    volumes:
      - /home/data/nacos-data/logs:/home/nacos/logs
      - /usr/share/zoneinfo/Asia/Shanghai:/usr/share/zoneinfo/Asia/Shanghai
    # 服务编排
    depends_on:
      - mysql
networks:
  by-network:
    driver: bridge
```

###  2.1 搭建过程可能遇到的问题

###  2.1.1 网络配置
整套服务目前使用别名代替ip,所有服务使用[别名]:[端口]的访问方式。  
所有服务尽可能的使用bridge网络模式。
- 由于host使用主机网络的限制，所以无法加入到其他网络。
- 而且必须使用，自建的bridge网络
- 由于默认的bridge是直接copy的宿主机的nds配置文件，而自建的网络会自带一个docker dns server，所以可以使用别名

### 2.1.2 环境变量配置
```yaml
 environment:
      - TZ='Asia/Shanghai'
      - MODE='standalone'
      - MYSQL_DATABASE_NUM=1
      - SPRING_DATASOURCE_PLATFORM='mysql'
      - MYSQL_SERVICE_HOST='mysql'
```
千万别使用上面的配置方式  
```yaml
 environment:
      - TZ=Asia/Shanghai
      - MODE=standalone
      - MYSQL_DATABASE_NUM=1
      - SPRING_DATASOURCE_PLATFORM=mysql
      - MYSQL_SERVICE_HOST=mysql
```

### 2.1.3 nacos 报错
`The Raft Group [naming_instance_metadata] did not find the Leader node`  
如果是部署全新的nacos，可以不用关注这个问题。    
若是偷懒，用以前的nacos配置，那么需要进入nacos容器/data目录删除protocol文件夹再重启。


## 2.2 服务管理
```shell
docker pull registry.cn-hangzhou.aliyuncs.com/voovan/dockerfly:20170227
docker tag registry.cn-hangzhou.aliyuncs.com/voovan/dockerfly:20170227 dockerfly
docker run --name dockerfly --restart=always -d -v /var/run/docker.sock:/var/run/docker.sock -p 2735:2735 -p 28083:28083 dockerfly
```
浏览器打开
http://[ip]:28083/
`` 



