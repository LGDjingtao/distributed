# 单节点搭建流程

# 1.私有仓库
## 1.1 私有搭建
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
## 1.2私仓使用

### 1.2.1 地址配置
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
1.docker版本过低，需要在docker扩展启动配置私有仓库地址
```shell
sudo vim /usr/lib/systemd/system/docker.service
```
在启动文件中配置私仓地址  `ExecStart=` 加上  `--insecure-registry=【ip】:5000`
```properties
ExecStart=/usr/bin/dockerd -H fd:// --containerd=/run/containerd/containerd.sock --insecure-registry=【ip】:5000
```
2.配置多个私仓冲突  
删除之前的私仓，再重启

### 1.2.2 镜像推送和拉取
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

### 1.2.2 私有仓库的管理
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

# 2.服务搭建
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

##  2.1 搭建过程可能遇到的问题

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

# 3.自动部署
一键部署以出入口系统为例子
## 3.1 步骤一
在docker-compose.yml文件的同目录下建一个固定文件名 `.env`.  
填入内容如下：
```properties
RALID_ENTRNCE=20231027-155145
```

## 3.2 步骤二
用外部变量`${RALID_ENTRNCE}`替换镜像版本号
```yml
  省略: ....
  sn-ralid-entrance:
    image: 192.168.1.240:5000/sn-ralid-entrance:${RALID_ENTRNCE}
    container_name: sn-ralid-entrance
    restart: always
    networks:
      - by-network
    ports:
      - "30012:30012"
    volumes:
      - /home/senergy/airport-data/sn-senergy-cold-data/logs:/senergy/logs
    depends_on:
      - mysql
      - redis
networks:
  by-network:
    driver: bridge
```
可以使用如下命令，预览yml文件
```shell
docker-compose config
```
## 部署脚本
```shell
#!/bin/bash
DATE_TIME=`date +%Y%m%d-%H%M%S`
echo '构建镜像 ===> sn-ralid-entrance:'${DATE_TIME}
docker build -t  sn-ralid-entrance:${DATE_TIME} . 
echo '修改镜像tag ====>  192.168.1.240:5000/sn-ralid-entrance:'${DATE_TIME}
docker tag sn-ralid-entrance:${DATE_TIME} 192.168.1.240:5000/sn-ralid-entrance:${DATE_TIME}
echo '推送镜像到私有仓库 =====> 192.168.1.240:5000'
docker push 192.168.1.240:5000/sn-ralid-entrance:${DATE_TIME} 
source  /home/data/.env

echo 'sn-ralid-entrance:旧版本号='   $RALID_ENTRNCE

sed -i  '1c RALID_ENTRNCE='${DATE_TIME}  /home/data/.env

source /home/data/.env

echo 'sn-ralid-entrance:新版本号='   $RALID_ENTRNCE

echo '执行docker-compose脚本'
cd /home/data/
docker-compose up -d
```
```text
注意了！！！！！！！
在新部署服务的时候
需要修改脚本替换为自己服务信息
特别是  
sed -i  '1c RALID_ENTRNCE='${DATE_TIME}  /home/data/.env

1c 代表第一行
自己服务版本号填在第几行，就填多少
例如 
.env文件现在内容为
RALID_ENTRNCE=20231027-155145
SENERGY_COLD=20231027-165215

第2行为冷源系统的版本号
那么脚本命令为
sed -i  '2c SENERGY_COLD='${DATE_TIME}  /home/data/.env
```

# 服务依赖
使用`healthcheck`解决服务之间依赖问题
```yaml
version: "3.8"
services:
  mysql:
    image: 192.168.1.240:5000/mysql:8.0.31
    container_name: mysql
    restart: always
    networks:
      - 省略
    ports:
      - "3306:3306"
    environment:
      - 省略
    volumes:
      - 省略
    healthcheck:
      # mysql ${MYSQL_DATABASE} --user=${MYSQL_USER} --password='${MYSQL_PASSWORD}' --silent --execute "SELECT 1;" 可以配置变量
      test: mysql byjc --user=root --password='user*2023' --silent --execute "SELECT 1;"
      # 每隔1秒检测一次 
      interval: 1s
      # 超时时间为3秒
      timeout: 3s
      # 最多重试30次
      retries: 30
      # start_period: 40s # 容器启动后多久开始检测  (default: 0s) 暂时不用
  nacos:
    image: 192.168.1.240:5000/nacos/nacos-server:v2.1.0
    container_name: nacos
    restart: always
    networks:
      - 省略
    ports:
      - 省略
    environment:
      - 省略
    volumes:
      - 省略
    healthcheck:
      test: ["CMD-SHELL", "curl -sS nacos:8848/nacos || exit 1"]
      interval: 10s
      timeout: 5s
      retries: 30
    depends_on:
      mysql:
        condition: service_healthy
        # 依赖服务更新后，true会重新启动该服务
        restart: true
```
上面例子，mysql使用healthcheck进行健康检查。
```shell
    healthcheck:
      test: ["CMD-SHELL", "curl -sS 127.0.0.1:9200 || exit 1"] # 检测方式
      interval: 1m30s  # 多次检测间隔多久 (default: 30s)
      timeout: 10s # 超时时间 (default: 30s)
      retries: 3 # 尝试次数(default: 3)
      start_period: 40s # 容器启动后多久开始检测  (default: 0s)
```
nacos使用depends_on强依赖于mysql
```text
在depends_on中，可以通过添加一个condition属性来指定服务之间的启动条件。该condition属性可以接受三个值；
 
condition: service_started 表示在依赖的服务启动之后，才启动本服务；
condition: service_healthy 表示在依赖的服务健康检查通过之后，才启动本服务；
condition: service_completed_successfully 表示在依赖的服务成功执行之后，才启动本服务。
```
nacos启动连不上mysql问题，在测试环境下新启动的mysql不能立马连接，解决方法就是在检查检查的时候去唤醒mysql
加了 --host参数，去通过docker网络去唤醒刚起来的mysql
```shell
    healthcheck:
      # mysql ${MYSQL_DATABASE} --user=${MYSQL_USER} --password='${MYSQL_PASSWORD}' --silent --execute "SELECT 1;" 可以配置变量
      test: mysql byjc --host mysql --user=root --password='user*2023' --silent --execute "SELECT 1;"
      # 每隔1秒检测一次 
      interval: 1s
      # 超时时间为3秒
      timeout: 3s
      # 最多重试30次
      retries: 30
      # start_period: 40s # 容器启动后多久开始检测  (default: 0s) 暂时不用

```