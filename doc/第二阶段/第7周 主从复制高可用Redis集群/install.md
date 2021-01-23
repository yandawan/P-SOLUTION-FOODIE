# 配置文件 redis.conf
```
# 前台还是后台运行 yes 后台运行 no 前台运行
daemonize yes
# 当前redis 的当前的工作目录
dir /usr/local/redis/working
# 允许远程访问
bind 0.0.0.0
# 设置密码
requirepass imooc
```

# 配置 redis_init_script
```
cd /etc/init.d
vi redis_init_script
```

# 常用数据类型设置
```
# 如果不存在就设置
setnx age 18
get age

# 设置过期时间



```