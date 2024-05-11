# 1. 拉镜像
自带jdk11的jenkins
```shell
docker pull jenkins/jenkins:lts-jdk11
```
docker-compose
```yml
  jenkins:
    image: jenkins/jenkins:lts-jdk11
    container_name: jenkins
    restart: always
    #给权限
    privileged: true
    networks:
      - tt-network
    ports:
      - "8080:8080"
      - "50000:50000"
    volumes:
      # 获取宿主机docker权限
      - /var/run/docker.sock:/var/run/docker.sock
      - /usr/bin/docker:/usr/bin/docker
      - /etc/sysconfig/docker:/etc/sysconfig/docker
      - /data/jenkins-data:/var/jenkins_home
      - /data/maven-data/apache-maven-3.8.8:/usr/local/maven/apache-maven-3.8.8
      # 持久化jenkins 数据
      - /data/jenkins-data:/var/jenkins_home
```



# 2. 权限问题
## 1.jenkins 数据持久话权限
持久化目录  
```yml
- /data/jenkins-data:/var/jenkins_home
```
给持久化目录权限
```shell
chown -R 1000:1000 /data/jenkins-data/
```

## 2.docker 宿主机权限开发
```shell
chown .1000 /var/run/docker.sock
```

# 3. 开始构建

## 1.源码管理
![imgjinkins.png](img%2Fimgjinkins.png)

## 2. mavne 命令
![imgmaven.png](img%2Fimgmaven.png)

## 3.shell
```shell
#!/bin/bash 
result=$(docker ps | grep "jenkins-test") 
if [[ "$result" != "" ]] 
then 
echo "stop jenkins-test" 
docker stop jenkins-test 
fi
result1=$(docker ps -a | grep "jenkins-test") 
if [[ "$result1" != "" ]] 
then 
echo "rm jenkins-test" 
docker rm jenkins-test
fi
result2=$(docker images | grep "jenkins-test") 
if [[ "$result2" != "" ]] 
then 
echo "jenkins-test:latest" 
docker rmi jenkins-test:latest
fi
```
启动脚本
```shell
sh update.sh
```
  