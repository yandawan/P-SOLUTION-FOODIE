# mycat

## 管理端口 9066
```
# 使用命令 reload 不用重新启动mycat
# 显示帮助文档
show @@help
# 显示数据节点
show @@datanote
# 重新加载配置文件
reload @@config
# 如果修改的是数据源相关的东西
reload @@config_all
```

## proxy 和 nginx 
```
proxy 支持 tcp/http
nginx 支持 http
```

## sharding-jdbc
```
可以在哦那个一个数据库中进行分片
```

## 区别与联系
```
运维强 选mycat 
运维弱 选sharding-jdbc
```