# 配置上游服务器
```
upstream gravity {
    server 192.168.72.132:8011 max_fails=2 fail_timeout=1s;
    server 192.168.72.132:8012 max_conns=2;
    server 192.168.72.132:8013 max_conns=5;
}

server {
    listen       80;
    # 填写成域名
    server_name  localhost;

    location / {
        add_header Access-Control-Allow-Origin "*";
        add_header Access-Control-Allow-Credentials "true";
        add_header Access-Control-Allow-Methods "GET,PUT,POST,DELETE,OPTIONS";
        add_header Access-Control-Allow-Headers "Origin,X-Requested-With,Content-Type,Accept,Authorization";
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_pass http://gravity;
    }
}

# 表示服务器不能够使用
max_fails 最大失败次数
fail_timeout 超时时间

http://127.0.0.1:8810
```