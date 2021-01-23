# keepalived-db1

```
yum  -y install ipvsadm
ssh root@10.211.55.23
```

## 安装
```
yum -y install keepalived

cd /etc/keepalived/
清除原内容
> keepalived.conf
vi /etc/keepalived/keepalived.conf

! Configuration File for keepalived

# 全局配置
global_defs {
   # 通知邮件
   notification_email {
     acassen@firewall.loc
     failover@firewall.loc
     sysadmin@firewall.loc
   }
   notification_email_from Alexandre.Cassen@firewall.loc
   smtp_server 192.168.200.1
   smtp_connect_timeout 30
   # 路由id 当前安装 keepalived节点主机的标识符 全局唯一
   router_id 10.211.55.26
   vrrp_skip_check_adv_addr
   vrrp_strict
   vrrp_garp_interval 0
   vrrp_gna_interval 0
}

vrrp_script chk_mysql {          # 检测mysql服务是否在运行。有很多方式，比如进程，用脚本检测等等
    script "/opt/chk_mysql.sh"   # 这里通过脚本监测（见9.1）
    interval 2                   # 脚本执行间隔，每2s检测一次
    weight -5                    # 脚本结果导致的优先级变更，检测失败（脚本返回非0）则优先级 -5
    fall 2                       # 检测连续2次失败才算确定是真失败。会用weight减少优先级（1-255之间）
    rise 1                       # 检测1次成功就算成功。但不修改优先级
}

# 计算机节点
vrrp_instance VI_1 {
    # 表示的状态是 MASTER, 其他的状态是 MASTER/BACKUP
    state MASTER
    # 当前实例绑定的网卡，需要注意 有可能是 ens33
    interface eth0
    # 虚拟路由id: 保证主备节点一致
    virtual_router_id 51
    # 优先级/权重: 谁的优先级 在Master 挂掉以后就能成为 Master
    priority 100
    # 主备之间同步检查的时间间隔,默认 1秒
    advert_int 1
    nopreempt
    # 认证授权的密码,防止非法节点的进入,主备之间相同就可以了
    authentication {
        auth_type PASS
        auth_pass 1111
    }
    # 虚拟 IP
    virtual_ipaddress {
        10.211.55.88
    }
    track_script {
       chk_mysql
    }
}

vrrp_instance VI_2 {
    state BACKUP
    interface eth0
    virtual_router_id 52
    priority 80
    advert_int 1
    nopreempt
    authentication {
        auth_type PASS
        auth_pass 1111
    }
    # 虚拟 IP
    virtual_ipaddress {
        10.211.55.87
    }
}
```

## 创建检测脚本
```
宿主机安装了mysql客户端:
# 以前的安装方式
# 安装 pip 命令
# wget https://bootstrap.pypa.io/get-pip.py
# python get-pip.py

# yum install mysql-devel
# yum install gcc libffi-devel python-devel openssl-devel
# pip install mysqlclient

yum install -y mariadb.x86_64 mariadb-libs.x86_64

创建心跳检测账号
docker exec -it mysql_master10 /bin/bash
mysql -u root -p
create user 'keepalived_heartbeat'@'10.211.55.23' identified by '123456';
flush privileges;
# 删除用户
# 显示数据库
show databases;
# 切换数据库
use mysql;
delete from user where user='keepalived_heartbeat' and host='10.211.55.23';

测试脚本
mysqladmin -u keepalived_heartbeat -p123456 -h10.211.55.23 --port=3310 ping

#! /bin/bash
#! /bin/bash
MYSQL_PASS=123456
MYSQL_PING=`mysqladmin -u keepalived_heartbeat -p${MYSQL_PASS} -h10.211.55.23 --port:3310 ping`
MYSQL_OK="mysqld is alive"

if [[ "$MYSQL_PING" != "$MYSQL_OK" ]]
    then
        killall keepalived    
    else
        echo "mysql is ok"
fi

设置权限
cd /opt/
sudo chmod -R 777 chk_mysql.sh
```

## 启动
```
systemctl start keepalived
systemctl restart keepalived
systemctl status keepalived
systemctl stop keepalived

# 查看虚拟ip 是否已经绑定上
ip addr
# 查看进程
ps -ef|grep keepalived 
```