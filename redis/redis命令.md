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

