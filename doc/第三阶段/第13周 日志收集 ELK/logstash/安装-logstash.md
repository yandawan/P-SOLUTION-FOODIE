# 1 准备工作 - logstash:7.x 有坑
```
1 存在的mysql的数据库表
2 创建好的elasticsearch索引
```

# 2 下载
```
1 docker pull logstash:6.7.1
2 下载mysql驱动 - 选平台无关的
  mysql-connector-java-8.0.17.jar
```

# 3 安装
```
mkdir -p /Users/ivy-dev/Desktop/java/logstash-ik

# 简单运行
docker run -tid \
--restart=always \
--name=logstash_test \
-p 5044:5044 \
-p 9600:9600 \
logstash:6.7.1

# 文件复制
# 进入容器查看配置文件
docker exec -it logstash_test /bin/bash
# 拷贝 logstash.conf
docker cp logstash_test:/usr/share/logstash/pipeline /Users/ivy-dev/Desktop/java/logstash-ik/pipeline && \
docker cp logstash_test:/usr/share/logstash/config /Users/ivy-dev/Desktop/java/logstash-ik/config

# 将文件拷贝到文件夹中: /Users/ivy-dev/Desktop/java/logstash-ik/config
foodie-items.sql
logstash-ik.json
mysql-connector-java-8.0.17.jar

# 文件修改
# 修改logstash.conf
使用 logstash.conf 中的文件

# 修改 logstash.yml 中的文件
xpack.monitoring.elasticsearch.hosts: [ "http://192.168.1.3:9200" ]

# 暂停移除测试容器
docker stop logstash_test && docker rm logstash_test

docker run -d --restart=always --privileged=true -p 5044:5044 -p 9600:9600 --name logstash \
-v /Users/ivy-dev/Desktop/java/logstash-ik/pipeline:/usr/share/logstash/pipeline \
-v /Users/ivy-dev/Desktop/java/logstash-ik/config:/usr/share/logstash/config \
logstash:6.7.1

docker exec -it logstash /bin/bash
docker stop logstash && docker rm logstash

docker logs logstash

# 403 问题解决
curl -XPUT -H 'Content-Type: application/json' http://192.168.1.3:9200/_all/_settings -d '{"index.blocks.read_only_allow_delete": null}'

```