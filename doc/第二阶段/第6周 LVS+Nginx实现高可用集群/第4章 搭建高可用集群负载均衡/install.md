# 说明
```
阿里云不支持虚拟IP 需要购买响应的负载均衡服务
腾讯云 支持虚拟IP 但是一台机子只支持10个
```

# LVS 安装
```
# 1 停止网络管理器 - 可以跳过此步
systemctl stop NetworkManager
systemctl disable NetworkManager
```

# 虚拟ip设置
# 192.168.157.142
## 1 设置虚拟IP
```
cd /etc/sysconfig/network-scripts
```

## 2 拷贝一份新的网卡配置文件
```
cp ifcfg-ens33 ifcfg-ens33:1
vi ifcfg-ens33:1

BOOTPROTO=static
DEVICE=ens33:1
ONBOOT=yes
NETMASK=255.255.255.0
IPADDR=192.168.157.150

# 重启网络生效
service network restart
# 查看网络
ip addr

# 3 安装集群管理工具
yum install ipvsadm
# 查看 
ipvsadm -Ln
```

# 192.168.157.139 - 安装有Nginx
## 1 设置虚拟IP
```
cd /etc/sysconfig/network-scripts
```

## 2 拷贝一份新的网卡配置文件
```
cp ifcfg-lo ifcfg-lo:1
vi ifcfg-lo:1

DEVICE=lo:1
IPADDR=192.168.157.150
NETMASK=255.255.255.255
NETWORK=127.0.0.0
BROADCAST=127.255.255.255
ONBOOT=yes
NAME=loopback

# 重启网络生效
ifup lo
# 查看网络
ip addr
```

# 192.168.157.138 - 安装有Nginx
## 1 设置虚拟IP
```
cd /etc/sysconfig/network-scripts
```

## 2 拷贝一份新的网卡配置文件
```
cp ifcfg-lo ifcfg-lo:1
vi ifcfg-lo:1

DEVICE=lo:1
IPADDR=192.168.157.150
NETMASK=255.255.255.255
NETWORK=127.0.0.0
BROADCAST=127.255.255.255
ONBOOT=yes
NAME=loopback

# 重启网络生效
ifup lo
# 查看网络
ip addr
```

# ARP 响应级别和通告行为
# 192.168.157.139 - 安装有Nginx
## 1 设置虚拟IP
```
vi /etc/sysctl.conf

net.ipv4.conf.all.arp_ignore = 1
net.ipv4.conf.default.arp_ignore = 1
net.ipv4.conf.lo.arp_ignore = 1

net.ipv4.conf.all.arp_announce = 2
net.ipv4.conf.default.arp_announce = 2
net.ipv4.conf.lo.arp_announce = 2

# 刷新
sysctl -p
```

## 2 添加路由
```
route add -host 192.168.157.150 dev lo:1

# 查看
route -n

# 添加开机自启动
echo "route add -host 192.168.157.150 dev lo:1" >> /etc/rc.local
```

# 192.168.157.138 - 安装有Nginx
## 1 设置虚拟IP
```
vi /etc/sysctl.conf

net.ipv4.conf.all.arp_ignore = 1
net.ipv4.conf.default.arp_ignore = 1
net.ipv4.conf.lo.arp_ignore = 1

net.ipv4.conf.all.arp_announce = 2
net.ipv4.conf.default.arp_announce = 2
net.ipv4.conf.lo.arp_announce = 2

# 刷新
sysctl -p
```

## 2 添加路由
```
route add -host 192.168.157.150 dev lo:1

# 查看
route -n

# 添加开机自启动
echo "route add -host 192.168.157.150 dev lo:1" >> /etc/rc.local
```

# 配置集群的规则
```
# 查看命令行
ipvsadm -h

# 构建集群路由
ipvsadm -A -t 192.168.157.150:80 -s rr 
# 查看
ipvsadm -Ln

ipvsadm -a -t 192.168.157.150:80 -r 192.168.157.139:80 -g
ipvsadm -a -t 192.168.157.150:80 -r 192.168.157.138:80 -g

ipvsadm -Ln
ipvsadm -Ln --stats
```

# 修改
```
# 因为轮询有时间的限制 会打到同一台机子上

# 修改轮训时间为5s
ipvsadm -E -t 192.168.157.150:80 -s rr -p 5
ipvsadm -Ln

# 设置tcp 超时时间
ipvsadm --set 1 1 1

# 查看访问结果
ipvsadm -Lnc
```


