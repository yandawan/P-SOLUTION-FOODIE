# RabbitMQ

## 安装
```$xslt
docker run -dit --name shop-rabbitmq \
-p 15672:15672 \
-p 5672:5672 \
--restart=always \
 --privileged=true \
-e RABBITMQ_DEFAULT_USER=root \
-e RABBITMQ_DEFAULT_PASS=root \
-e RABBITMQ_DEFAULT_VHOST=/  \
rabbitmq:3.7.7-management

RABBITMQ_DEFAULT_VHOST：默认虚拟机名
RABBITMQ_DEFAULT_USER：默认的用户名
RABBITMQ_DEFAULT_PASS：默认用户名的密码
```

# 访问
```
http://127.0.0.1:15672
```