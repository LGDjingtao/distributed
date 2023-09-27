# 清理overlay2日志

## 基础命令
查看当前目录下面的文件的磁盘占用量  
`ls -flh`  
查看当前目录下面所有文件夹的磁盘占用情况  
`du -h --max-depth=1`
查看某个目录所在磁盘的使用量  
`df -h [目录名]`

## 清理流程

### 查看
`df -h查看磁盘占用空间发现
/var/lib/docker/overlay2 占满了
` 
![img_1.png](img%2Fimg_1.png)  

### 查询overlay2是属于哪个容器
docker ps -q | xargs docker inspect --format '{{.State.Pid}}, {{.Name}}, {{.GraphDriver.Data.WorkDir}}' | grep "dfdd4dabc960b699b290e3d9328b34172584918aa14825aecf67de16546c1f7d"
![img_2.png](img%2Fimg_2.png)

### 进入docker容器目录删除日志文件
