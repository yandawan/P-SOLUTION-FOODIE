# 批量查询
```
public Object mget(String... keys){
   List<String> keyList = Arrays.asList(keys);
   return redisOperator.mget(keyList);
}

严格来说上述命令不属于批量操作，而是在一个指令中处理多个key

优点:
性能优异，因为是单条指令操作，因此性能略优于其他批量操作指令

缺点:
批量命令不保证原子性，存在部分成功部分失败的情况，需要应用程序解析返回的结果并做相应处理

批量命令在key数目巨大时存在RRT与key数目成比例放大的性能衰减，会导致单实例响应性能(RRT)严重下降，
更多分析请参考之前的文章

集群行为: 
客户端分片场景下，Jedis不支持客户端mget拆分，需要在业务代码中根据分片规则自行拆分并发送到对应得redis实例，会导致业务逻辑代码中夹杂着jedis分片逻辑
中间件分片场景下，Codis等中间件分片服务中，会将mget/mset的多个key拆分成多个命令发往不同得redis实例，事实上已经丧失了mget强大的聚合执行能力
Cluster场景下，mget仅支持单个slot内批量执行，否则将会获得一个错误信息
```