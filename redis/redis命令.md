# redis命令

## String
`redis 字符串可以存储3种类型的值`  
`字节型`    
`整数`  
`浮点型`  

### redis对字符串类型存储整形 有以下这些命令
`INCR key-name -- 将键储存的值加上1`  
`DECR key-name -- 将键储存的值减去1`  
`INCRBY key-name amount -- 将键储存的值加上整数amount`  
`DECRBY key-name amount -- 将键储存的值减去整数amount`  
`DECRBYFLOAT key-name amount -- 将键储存的值加上浮点数amount.这个命令在redis2.6上或者以上版本可用`  

### redis对字符串类型存储字串和二进制位 有以下这些命令
`APPEND key-name value -- 将value追加到给定键key-name存储值的末尾`  
`GETRANGE key-name start end  -- 获取一个由偏移量start 至偏移量end 范围所有字符组成的字串，包括start和end`    
`SETRANGE key-name offset value -- 将从offset 偏移量开始的字串替换给定值(会覆盖后面的值)`      
`GETBIT key-name offset value -- 将字节串看作二进制位串，并返回位串中偏移量为offset的二进制的值`  
`SETBIT key-name offset value -- 将字节串看作二进制位串，并将位串中偏移量为offset的二进制位的值设置为value`  
`BITCOUNT key-name [start end] `
`BITOP operation dest-key key-name [key-name ...] --对一个或者多个二进制串位执行包括AND  OR  XOR  NOT  在内的任意一种按位运算
得出的结果 保存在dest-key键里面`  

## List

### list基础命令
`RPUSH key-name value [value ...]  -- 将一个或者多个值推入列表右端`  
`LPUSH key-name value [value ...]  -- 将一个或者多个值推入列表左端`  
`RPOP key-name -- 移除并返回列表最右端的的元素`  
`LPOP key-name -- 移除并返回列表最左端的的元素`  
`LINDEX key-name offset -- 返回列表中偏移量为offset的元素`  
`LRANGE start end -- 返回列表从start偏移量 到end偏移量范围内的所有元素 其中偏移量为start和end的原始也会包含其中`  
`LTRIM key-name start end -- 对列表进行修剪，只保留从start偏移量到end偏移量范围内的元素，start end元素也会保留`  

### list阻塞式命令 以及
`BLPOP  key-name [key-name ...] timeout -- 从第一个非空列表中弹出位于最左端的元素，或者在timeout秒之内阻塞并等待可弹出的元素出现`  
`BRPOP  key-name [key-name ...] timeout -- 从第一个非空列表中弹出位于最右端的元素，或者在timeout秒之内阻塞并等待可弹出的元素出现`  
`RPOPLPUSH source-key dest-key -- 从source-key 列表弹出位于最右端的元素，然后将这个元素推入dest-key，并向用户返回这个元素`  
`BRPOPLPUSH source-key dest-key timeout --  从source-key 列表弹出位于最右端的元素，然后将这个元素推入dest-key，并向用户返回这个元素 如果source-key为空，那么在timeout之内阻塞并等待可弹出的元素出现`    


## set

### set基础命令
`SADD key-name item [item ...] -- 将一个或多个元素添加到集和里，并返回被被添加元素当中原本不存在于集和里面的元素个数`  
`SREM key-name item [item ...] -- 从集和里面移除一个或者多个元素，并返回被移除的元素的数量`  
`SISMEMBER key-name item -- 检查元素item是否存在于集和key-name里面`  
`SCARD key-name -- 返回集和包含的元素数量`  
`SMEMBERS key-name -- 返回集和包含的所有元素`  
`SRANDMEMBER key-name [count] -- 从集和里随机返回一个或多个元素. 当count为正数时，命令返回的随机元素不会重复 为负数时可能会重复`
`SPOP key-name -- 随机地移除元素一个元素，并返回被移除的元素 `  
`SMOVE source-key dest-key item -- 如果集和source-key包含元素item，那么从集和source-key里面移除元素item，并将元素item添加到集和dest-key中；
如果item被移除成功，那么命令返回1，否则返回0`
``

### set用于组合和处理多个集和
`SDIFF key-name [key-name ...] -- 返回那些存在于一个集和，但是不存在于其他集和中的元素(数学上的差集)`  
`SDIFFSTORE dest-key key-name [key-name ...] 将那些存在于第一个集和但并不存在于其他的集和中的元素(数学上的差集运算) 存储到dest-key键里面`  
`SINTER key-name[key-name ...] -- 返回那些同时存在于所有集和中的元素`  
`SINTERSTORE dest-name key-name [key-name ...] -- 将那些同时存在于所有集和中的元素存到dest-name集和中`  
`SUNION key-name [key-name ...] -- 返回那些至少存在于所有集和中的元素(数学上的并集计算)`  
`SUNIONSTORE dest-key [key-name ...] -- 将那些至少存在于一个集和中的元素（数学上的并集计算） 存储到dest-key键里面`


## hash

### 常用基础命令
`HMGET key-name key [key ...] -- 从散列里面获取一个或者多个键的值`  
`HMSET key-name key value [key value ...] -- 为散列里面的一个或者多个键设置值`  
`HDEL key-name key [key ...] -- 删除散列里面 的一个或者多个键值对 ， 返回成功找到并删除键值对数量`  
`HLEN key-name -- 返回散列包含的键值对数量`  

### hash的高级特性
`HEXISTS key-name key -- 检查给定键是否存在于散列中`  
`HKEYS key-name -- 获取散列包含的所有键`    
`HVALS key-name -- 获取散列包含的所有值`  
`HGETALL key-name -- 获取包含所所有的键值对`  
`HINCRBY key-name key increment -- 将键key 存储的值加上整数increment`
`HINCRBYFLOAT key-name key increment -- 将键key 存储的值加上浮点数increment`  


## zset

### 常用命令
`ZADD key-name score member [score member ...]  -- 将带有给定分值的成员添加到有序集和里面`  
`ZREM key-name member [member ...] -- 从有序集和里面移除指定成员，并返回被已移除成员数量`  
`ZCARD key-name -- 返回有序集和包含的成员数量`  
`ZINCRBY key-name increment menber -- 从有序集和里面移除给定的成员，并返回被移除成员数量`  
`ZCOUNT key-name min max -- 返回分值介于min和max之间的成员数量`  
`ZRANK key-name member -- 返回成员member在有序集和中的排名`  
`ZSCORE key-name member -- 返回成员member的分值`  
`ZRANGE key-name start stop [WITHSCORES] -- 返回有序集和中排名介于start和stop之间的成员，如果给定了可选的WITHSCORES选项，那么命令会将成员的分值也一并返回`  

### 有序集和的范围型数据获取命令和范围型数据删除命令，以及并命令和交集命令
`ZREVRANK key-name member -- 返回有序集和成员member的排名，成员按分值从大到小排列`  
`ZREVRANGE key-name start stop [WITHSCORES] -- 返回有序集和给定排名范围内的成员，成员按照分值从大到小排列`  
`ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT offset count]-- 返回有序集合中，分值介于min和max的所有成员`
`ZREVRANGEBYSCORE key max min [WITHSCORES] [LIMIT offset count]-- 获取有序集合中分值介于min和max之间，并按照分值从大到小的顺序来返回他们`
`ZREMRANGEBYRANK key-name start stop -- 移除有序集合中排名介于start 和 stop 之间的所有成员`  
`ZREMRANGEBYSCORE key-name min max -- 移除有序集合中分值介于min 和 max 之间的所有成员`   
`ZINTERSTORE dest-key key-count key [key ...] [WEIGHTS weight [weight ...]] [AGGREGATE SUM|MIN|MAX] -- 对给定的有序集和执行类似于集和的交集运算`  
`ZUNIONSTORE dest-key key-count key [key ...] [WEIGHTS weight [weight ...]] [AGGREGATE SUM|MIN|MAX] -- 对给定的有序集和执行类似于集和的交集运算`  


## 发布与订阅
`SUBSCRIBE channel [channel ...] -- 订阅给定的一个或多个频道`  
`SUBSCRIBE [channel [channel ...]] -- 退订给定的一个或多个频道，如果执行时没有给定任何频道，那么退订所有频道`  
`PUBLISH channel message -- 向给定频道发消息`  
`PSUBSCRIBE pattern [pattern ...] -- 订阅与给定模式匹配的所有频道`  
`PUNSUBSCRIBE [pattern [pattern ...]] -- 退订给定模式，如果执行时没有给定任何模式，那么退订所有模式`  

## 


