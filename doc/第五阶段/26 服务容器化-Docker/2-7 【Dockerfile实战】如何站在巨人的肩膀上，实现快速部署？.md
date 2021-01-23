```
COPY VS ADD

1 ADD 解压功能
2 ADD 下载功能

EXPOSE

ENTRYPOINT ["/bin/sh"] 永远会执行
CMD ["/bin/sh"] 如果有 ENTRYPOINT CMD 就作为ENTRYPOINT的参数 和 ENTRYPOINT 一起执行，如果没有  ENTRYPOINT 就 CMD单独执行
```