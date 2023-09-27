# 创建目录
分别在集群的各服务器（131/132/133）上创建目录:
sudo rm -rf /home/data/redis/ && sudo mkdir -p /home/data/redis/{7001,7002,7003,7004,7005,7006}/{data,conf} && chmod 777 -R /home/data/
# 创建网络
docker network create --driver overlay mynetwork
