创建或修改 /etc/docker/daemon.json 

`
{
"registry-mirrors": [
"https://registry.hub.docker.com",
"http://hub-mirror.c.163.com",
"https://docker.mirrors.ustc.edu.cn",
"https://registry.docker-cn.com"
]
}
`
systemctl restart docker
docker info
查看
`
Registry Mirrors:
https://registry.hub.docker.com/
http://hub-mirror.c.163.com/
https://docker.mirrors.ustc.edu.cn/
https://registry.docker-cn.com/

`