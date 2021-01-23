# 核心概念

## 1 Server
```
又称 Broker 接受客户端的连接 实现AMQP实体服务
```

## 2 Connection 
```
应用程序与Broker的网络连接
```

## 3 Channel 
```
网络信道 几乎所有的操作都在Channel 中进行 Channel 是进行
消息读写的通道 客户端可以建立多个Channel 每隔Channel代表一个会话
```

## 4 Message
```
消息 服务器和应用程序之间传送的数据 由 Properties 和Body组成
Properties 可以对消息进行修饰 比如消息的优先级 延迟等高级特性
Body则就是消息体内容
```

## 5 Virtual host 
```
虚拟地址 用于进行逻辑隔离 最上层的消息路由 
一个Virtual Host里面可以有若干个 Exchange 和 Queue, 同一个Virtual Host
里面不能有相同名称的 Exchange 或 Queue 
```

## 6 Exchange 
```
交换机 接收消息 根据路由键转发消息到绑定的队列
```

## 7 Binding 
```
Exchange Queue 之间的虚拟连接 binding 可以包含 routing key  
```

## 8 Routing key
```
一个路由规则 虚拟机可以它来确定如何路由一个特定消息
```

## 9 Queue
```
消息队列 保存消息并将他们转发给消费者
```