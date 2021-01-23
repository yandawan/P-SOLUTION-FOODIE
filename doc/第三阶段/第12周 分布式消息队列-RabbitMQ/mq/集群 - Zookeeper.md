```
mkdir -p /Users/ivy-dev/Desktop/java/zookeeper/zk1/data && \
mkdir -p /Users/ivy-dev/Desktop/java/zookeeper/zk1/datalog && \
mkdir -p /Users/ivy-dev/Desktop/java/zookeeper/zk2/data && \
mkdir -p /Users/ivy-dev/Desktop/java/zookeeper/zk2/datalog && \
mkdir -p /Users/ivy-dev/Desktop/java/zookeeper/zk3/data && \
mkdir -p /Users/ivy-dev/Desktop/java/zookeeper/zk3/datalog

# 创建网络
docker network create --driver bridge --subnet 172.55.0.0/25 --gateway 172.55.0.1  zookeeper_network

# 运行
docker-compose -f zk-docker-compose.yml up --force-recreate  
```