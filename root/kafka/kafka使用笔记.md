#  kafka使用笔记

## 使用架构
暂时使用架构是zookeeper + kafka  
kafka2.8以后可以不需要zookeeper  
选择老架构还是为了稳定性  

## 基础概念
### Producer 
消息的生产者

### cluster
kafka集群  
Producer是往cluster里面推送消息的

### broker 
缓存代理  
集群中的一台或者多台服务器统称broker  

### topic partition
topic：主题
partition: 分区
topic个人理解为：消息的逻辑分类。
每一个Topic被切分为多个Partitions
Broker Group中的每一个Broker保存Topic的一个或多个Partition

### group
消费者组  
有一个概念和限定很重要  
概念:一个消费者组对应kafka来说才是一个完整的消费者,简化来看,消费者组才是消费者  
限定:由于上面的限定,所以每个topic的一个partition只能由消费组里面的一个消费者来消费,这样确保整个消费者组不会消费重复的消息

## 安装和部署
这个在部署笔记上有写简单的安装部署,日后会更新小企业级的安装部署

## topic使用

### 命令行查看topic
进入这目录
`/opt/bitnami/kafka/bin`
老版本kafka 执行:  
`kafka-topics.sh -list --zookeeper zookeeper-server:2181`  
新版本kafka 执行:  
`kafka-topics.sh --list --bootstrap-server 127.0.0.1:9092`  

### 创建topic
创建一个名为firs的topic,一个分区,且没有加副本
`kafka-topics.sh  --bootstrap-server 127.0.0.1:9092  --create --topic firs --partitions 1`

### 查看topic描述
执行: 
`kafka-topics.sh  --bootstrap-server 127.0.0.1:9092  --describe --topic firs`
会显示下面这个描述
Topic: firs     Partition: 0    Leader: 1001    Replicas: 1001               Isr: 1001  
Topic:主题名           
Partition:分区名                           
Replicas:这个分区的副本在那个broker  

### 删除topic
`kafka-topics.sh  --bootstrap-server 127.0.0.1:9092  --delete --topic firs`  
这个删除不会立马删除topic,而是先标记删除,之后在某个时间节点才会去物理删除


###修改分区数
注意:分区数只能增加,不能减少
`kafka-topics.sh  --bootstrap-server 127.0.0.1:9092  --alter --topic firs  --partitions 2`

## Producer使用

### 发送消息
例如这里向zjttest这个topic里面发送3条消息  
`kafka-console-producer.sh  --bootstrap-server 127.0.0.1:9092 --topic zjttest`  
`>1`  
`>2`  
`>3`  

## consumer 使用

### 接收数据
`kafka-console-consumer.sh  --bootstrap-server 127.0.0.1:9092  --topic zjttest`  
注意,这样消费者不消费之前的数据  
如果想要消费之前的数据需要加参数--from-beginning  
`kafka-console-consumer.sh  --bootstrap-server 127.0.0.1:9092  --topic zjttest --from-beginning`  

## 工作流程
partition接收消息后会先去副本备份.

### group-consumer和consumer区别
消费组会实现断点续传功能,一个消费组里面的一个消费者挂了,这个消费组的另一个消费者可以从断点出开始消费.
而没消费组的消费者只会消费当前开始的topic消息,加--from-beginning参数才能消费之前消息




