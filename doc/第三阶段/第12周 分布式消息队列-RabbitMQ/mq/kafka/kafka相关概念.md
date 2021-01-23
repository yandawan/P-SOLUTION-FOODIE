# kafka 集群中的角色
```
1 producer
  消息生产者 发布消息到kafka集群的终端或者服务

2 broker
  kafka 集群中的服务器 一个broker代表kafka集群的一个节点

3 topic
  3.1 每条发不到kafka集群的消息属于的类别 某一类消息的聚集 例如: 订单topic 不同类别的消息会去往不同的topic, kafka是面向topic的
  3.2 topic就像一个消息队列 生产者根据消息的类别写往不同的topic 消费者从中读取消息
  3.3 一个topic支持多个订阅者同时订阅它

4 partition
  每个topic包含一个或者多个partition, kafka分配的单位是partition

5 replica
  partition的副本 保障kafka的高可用

6 consumer
  消费者 从 kafka 中消费消息的终端或者服务
  6.1 consumer 数量大于partition则有consumer空闲
  6.2 consumer 数量小于partition 则一个consumer消费多个partition
  6.3 consumer 数量等于partition 则一对一

7 consumer group
  每个consumer都属于一个consumer group
  1 同一个partition只能被同一个consumer group中的一个consumer消费
  2 同一个partition从可以被不同consumer group中的多个不同 consumer 消费
  一般同一个consumer group 处理同一个业务 比如统计订单数据，同一组的多个如果消费同一个
  partition 则可能导致数据统计重复

8 leader
  每个 topic 有至少一个 partition , 每个 partition 有且仅有一个leader
  leader 负责当前 partition 数据的读写 , 生产者和消费者只和leader互动

9 follow
  follower 跟随 leader, 所有写请求都通过 leader 路由, 数据变更会广播给所有 follower
  follower与leader 保持数据同步 如果 leader 失效, 则从 Follower 中选举出一个新的 leader

10 controller
   controller 主要负责kafka集群的管理 包含
   10.1 感知 broker 是否宕机
   10.2 leader 的选举
   10.3 partition 的选举
   10.4 对新增 broker 的监听
   10.5 新增加 broker 后数据的迁移和负载均衡
   10.6 kafka集群的各种元数据：如leader、partition、follower的位置等等
   10.7 删除 topic 后 partition 的删除
   负责管理整个集群的各种东西

zookeeper
   kafka 通过zookeeper来存储集群的元数据信息
   controller节点会在ZK上注册一个临时节点 其他节点监听该临时节点 当controller节点宕机后
   其他broker争抢再次创建临时节点 保证一台新的broker会成为controller角色

offset
   偏移量即消费者在对应分区上已经消费的消息数(位置) offset保存的地方和kafka版本有关系
   1 kafka0.8 版本之前offset 保存在zookeeper上
   2 kafka0.8 版本之后offset 保存在集群上

ISR 机制:
  ISR 机制主要是为了保证数据的不丢失 ISR列表中存放和leader数据完全同步的follower
  1 多副本机制能保证 kafka 的高可用 但不能保证数据不丢失 leader 宕机时 数据还没有被同步到其他
  follower 即使选举了新的leader 也会丢失没有同步部分的数据
  2 in-sync replica 就是跟leader partition保持同步的follower partition的数量 只有处于
  ISR列表中的follower 才可以在leader 宕机之后被选举为新的leader 因为在这个ISR列表里代表
  他的leader是同步的
```