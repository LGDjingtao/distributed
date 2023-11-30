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
