# 基本概念
```
1 Broker:
  1.1 一个Rabbitmq 可以称为一个 broker, 一般情况下当做是一个消息中间件的实例
  1.2 Rabbitmq将整个Broker划分为多个 Vhost, 可以认为是一个一个的逻辑的空间 这些逻辑空间是隔离的
  1.3 一般情况下 在一次的生产和消费的过程中只会使用一个Vhost
2 Vhost:
  Vhost是一个逻辑的空间,在这个逻辑的空间之中存在大量的exchange和queue

2 Exchange:
  消息交换机 它指定消息按什么规则, 路由到哪个队列

3 Queue:
  消息的载体 每个消息会被投到一个或者多个队列

4 Binding:
  绑定, 它的作用就是把 exchange 和 queue 按照路由规则绑定起来

5 Routing Key: 路由关键字 exchange 根据这个关键字进行消息的投递
7 Producer: 消息生产者 就是投递消息的程序
8 Consumer: 消息消费者 就是接受消息的程序
9 Channel: 消息通道 在客户端的每个连接里 可以建立多个channel
```