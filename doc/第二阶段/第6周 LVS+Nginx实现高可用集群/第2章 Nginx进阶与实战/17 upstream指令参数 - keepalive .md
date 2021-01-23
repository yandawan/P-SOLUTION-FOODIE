# 配置上游服务器
```
upstream gravity {
    server 192.168.72.132:8011 max_fails=2 fail_timeout=1s;
    server 192.168.72.132:8012 max_conns=2;
    server 192.168.72.132:8013 max_conns=5;

    # 长连接配置-1
    keepalive 32;
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

        # 长连接配置-2
        proxy_http_version 1.1;
        proxy_set_header Connection **;
    }
}

http://127.0.0.1:8810
```