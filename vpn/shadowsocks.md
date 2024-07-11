vim config.json
```json
{
    "server": "::",
    "server_port": 8388,
    "password": "=Xa9Z2E..%]QRPQ*",
    "method": "aes-256-gcm"
 }

```
```shell
docker run --name ssserver-rust \
  --restart always \
  -p 1128:8388/tcp \
  -p 1128:8388/udp \
  -v /data/shadowsocks/config.json:/etc/shadowsocks-rust/config.json \
  -dit ghcr.io/shadowsocks/ssserver-rust:latest

```