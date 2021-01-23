# 持久化
```
1 RDB
2 AOF
```

## RDB
```
相关的配置在: redis.conf

# 保存备份时间:默认
save 900 1
save 300 10
save 60 10000

# 设置持久化的保存文件
dbfilename dump.rdb

stop-writes-on-bgsave-error yes
rdbcompression yes
```

## AOF
```
可以设置备份时间和备份大小
```