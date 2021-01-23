# url_hash

# 配置上游服务器
```
upstream gravity {
    # 设置url hash
    least_conn;

    server 192.168.72.132:8011;
    server 192.168.72.132:8012;
    server 192.168.72.132:8013;
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

http://127.0.0.1:8810
```

# 计算
```
192.168.72
192.168.72   hash(192 168 1 )
192.168.72
```