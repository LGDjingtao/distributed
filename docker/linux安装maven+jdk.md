# 安装流程

## 1.jdk
### 1.下载解压
```shell
wget https://download.java.net/java/GA/jdk11/13/GPL/openjdk-11.0.1_linux-x64_bin.tar.gz
```

```shell
mkdir /usr/lib/jvm/
tar -zxf  openjdk-11.0.1_linux-x64_bin.tar.gz -C /usr/lib/jvm/
```

### 2. 环境变量
```shell
export JAVA_HOME=/usr/lib/jvm/jdk-11.0.1
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/jrt-fs.jar:$JAVA_HOME/libATH
```
改完记得加载一下配置
```shell
source /etc/profile
```





## 2.下载解压maven
```shell
wget http://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.8.8/binaries/apache-maven-3.8.8-bin.tar.gz
```

```shell
tar -xzvf apache-maven-3.3.9-bin.tar.gz
```

## 2.配置系统环境
修改如下文件
```shell
vi /etc/profile
```
并添加如下指令
```shell
export MAVEN_HOME=/home/maven/apache-maven-3.8.8
export PATH=$MAVEN_HOME/bin:$PATH
```
改完记得加载一下配置
```shell
source /etc/profile
```

检查是否生效
```shell
mvn -v
```