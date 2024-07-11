https 搭建



docker run -d \
-v /data/registry/data:/var/lib/registry \
-p 5000:443 \
-e REGISTRY_LOG_LEVEL=debug \
-e REGISTRY_HTTP_SECRET \
-e REGISTRY_HTTP_ADDR=0.0.0.0:443 \
-v /data/registry/cert:/certs \
-e REGISTRY_HTTP_TLS_CERTIFICATE=/certs/fullchain.pem \
-e REGISTRY_HTTP_TLS_KEY=/certs/privkey.pem \
--restart=always \
--name registry \
registry