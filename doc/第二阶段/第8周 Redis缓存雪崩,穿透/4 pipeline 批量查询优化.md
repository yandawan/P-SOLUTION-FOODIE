# 管道
```
public List<Object> batchGet(String... keys){
   List<Object> result = redisTemplate.executePipelined(new RedisCallback<String>() {
       public String doInRedis(RedisConnection connection) throws DataAccess{
           StringRedisConnection src = (StringRedisConnection)connection;
           for(String k: keys){
                src.get(key);
           }
           return null;
       }
   });
   return result;
}

管道(pipelining)方式意味着客户端可以在一次请求中发送多个命令。
例如在下例中，一次将多个命令传给redis，redis将在一个round trip中完成多命令并依次返回结果

优点:
通过管道，可以将多个redis指令聚合到一个redis请求中批量执行
可以使用各种redis命令，使用更灵活
客户端一般会将命令打包，并控制每个包的大小，在执行大量命令的场景中，可以有效提升运行效率
由于所有命令被分批次发送到服务器端执行，因此相比较事务类型的操作先逐批发送，再一次执行（或取消），管道拥有微弱的性能优势

缺点:
没有任何事务保证，其他client的命令可能会在本pipeline的中间被执行

集群行为:
客户端分片，需要由应用程序或client对命令按分片拆分并通过多个管道发送到不同的分片redis服务器执行
中间件分片，一般由中间件对管道进行拆分和结果合并
Cluster场景下，对pipeline的支持等同于单机，可以将同一节点中不同slot分片的节点通过批量操作一次执行，但是从实践来说，情况更加复杂，除非有充分的理由，否则不建议 (将来Jedis可能会支持对同一slot的所有key支持pipeline)
```