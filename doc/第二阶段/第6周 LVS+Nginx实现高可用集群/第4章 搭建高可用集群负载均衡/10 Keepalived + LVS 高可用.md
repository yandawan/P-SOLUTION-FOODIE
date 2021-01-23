# keepalived-MASTER

```
# 全局配置
global_defs {
   router_id 10.211.55.26
}

# 计算机节点
vrrp_instance VI_1 {
    state MASTER
    interface ens33
    virtual_router_id 41
    priority 100
    advert_int 1
    nopreempt
    authentication {
        auth_type PASS
        auth_pass 1111
    }
    # 虚拟 IP
    virtual_ipaddress {
        192.168.157.150
    }
}

# 接受监听数据来源的端口，网页入口使用
virtual_server 192.168.157.150 80 {
    #  健康检查的时间 单位:秒
    delay_loop 3
    # 负载均衡的算法 默认是轮询
    lb_algo rr 
    #  设置LVS的模式 NAT|TUN|DR
    lb_kind DR
    # 设置会话持久化时间
    persistence_timeout 5
    # 协议
    protocol TCP

    # 真实服务器1 也就是nginx 节点的具体的真实ip地址
    real_server 192.168.157.139 80 {
        weight 1
        # 设置健康检查
        TCP_CHECK {
            # 检查的 80 端口
            connect_port 80
            # 超时时间
            connect_timeout 80
            # 重试次数 2次
            nb_get_retry 2
            # 间隔时间 3s
            delay_before_retry 3
        }
    }

    # 真实服务器2 也就是nginx 节点的具体的真实ip地址
    real_server 192.168.157.138 80 {
        weight 1
        # 设置健康检查
        TCP_CHECK {
            # 检查的 80 端口
            connect_port 80
            # 超时时间
            connect_timeout 80
            # 重试次数 2次
            nb_get_retry 2
            # 间隔时间 3s
            delay_before_retry 3
        }
    }
}

# ipvsadm 清除
ipvsadm -C

ipvsadm -Ln
```

# keepalived-BACKUP

```
# 全局配置
global_defs {
   router_id 10.211.55.25
}

# 计算机节点
vrrp_instance VI_1 {
    state BACKUP
    interface ens33
    virtual_router_id 41
    priority 80
    advert_int 1
    nopreempt
    authentication {
        auth_type PASS
        auth_pass 1111
    }
    # 虚拟 IP
    virtual_ipaddress {
        192.168.157.150
    }
}

# 接受监听数据来源的端口，网页入口使用
virtual_server 192.168.157.150 80 {
    #  健康检查的时间 单位:秒
    delay_loop 3
    # 负载均衡的算法 默认是轮询
    lb_algo rr 
    #  设置LVS的模式 NAT|TUN|DR
    lb_kind DR
    # 设置会话持久化时间
    persistence_timeout 5
    # 协议
    protocol TCP

    # 真实服务器1 也就是nginx 节点的具体的真实ip地址
    real_server 192.168.157.139 80 {
        weight 1
        # 设置健康检查
        TCP_CHECK {
            # 检查的 80 端口
            connect_port 80
            # 超时时间
            connect_timeout 80
            # 重试次数 2次
            nb_get_retry 2
            # 间隔时间 3s
            delay_before_retry 3
        }
    }

    # 真实服务器2 也就是nginx 节点的具体的真实ip地址
    real_server 192.168.157.138 80 {
        weight 1
        # 设置健康检查
        TCP_CHECK {
            # 检查的 80 端口
            connect_port 80
            # 超时时间
            connect_timeout 80
            # 重试次数 2次
            nb_get_retry 2
            # 间隔时间 3s
            delay_before_retry 3
        }
    }
}

# ipvsadm 清除
ipvsadm -C

ipvsadm -Ln
```
