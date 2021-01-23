# 运行 tomcat
```
# 创建挂载文件夹
mkdir -p /Users/macuser/Desktop/java/tomcat1 && \
mkdir -p /Users/macuser/Desktop/java/tomcat2 && \
mkdir -p /Users/macuser/Desktop/java/tomcat3

docker run --name tomcat1 \
-p 8011:8080 \
-v /Users/macuser/Desktop/java/tomcat1:/usr/local/tomcat/webapps/test \
-d tomcat  

docker run --name tomcat2 \
-p 8012:8080 \
-v /Users/macuser/Desktop/java/tomcat2:/usr/local/tomcat/webapps/test \
-d tomcat  

docker run --name tomcat3 \
-p 8013:8080 \
-v /Users/macuser/Desktop/java/tomcat3:/usr/local/tomcat/webapps/test \
-d tomcat  

# 测试
http://localhost:8011/test/
http://localhost:8012/test/
http://localhost:8013/test/
```

# 配置nginx
```
vi /Users/macuser/Desktop/java/nginx/conf.d/default.conf

# 配置上游服务器
upstream gravity {
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