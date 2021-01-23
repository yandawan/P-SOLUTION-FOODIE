# 需要 添加ssl 模块
```
安装或者开启
```

## 配置
```
upstream gravity {
    server 192.168.72.132:8011;
    server 192.168.72.132:8012;
    server 192.168.72.132:8013;
}

# ssl 使用的端口 443
server {
    listen       443;
    # 填写成域名
    server_name  localhost;

    # 开启ssl 
    ssl  on;
    # 配置ssl证书
    ssl_certificate      /ssl/certificate.pem;
    # 配置证书密钥
    ssl_certificate_key  /ssl/2832429_fightingtop.cn.key;
    # ssl 会话 cache
    ssl_session_cache    shared:SSL:1m;
    # ssl 会话 超时时间
    ssl_session_timeout  5m;
    # 加密套件 写法
    ssl_protocols        TLSv1 TLSv1.1 TLSv1.2;
    ssl_ciphers          ECDHE-RSA-AES128-GCM-SHA256:ECDHE:ECDH:AES:HIGH:!NULL:aNULL:!MD5:!ADH:!RC4;
    ssl_prefer_server_ciphers  on;

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

server {
    listen 80;
    server_name fightingtop.cn www.fightingtop.cn;
    rewrite ^ https://$host$1 permanent;
}

http://127.0.0.1:8810
```
