# 启动控制台
```
java -Dserver.port=8111 \
-Dcsp.sentinel.dashboard.server=localhost:8111 \
-Dproject.name=sentinel-dashboard \
-Dsentinel.dashboard.auth.username=sentinel \
-Dsentinel.dashboard.auth.password=123456 \
-Dserver.servlet.session.timeout=7200 \
-jar sentinel-dashboard-1.8.0.jar
```