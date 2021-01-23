# 搜索引擎
```

```

## Lucene vs Solr vs Elasticsearch
```
1 倒排索引
2 Lucene是类库
3 Solr基于lucene
4 es基于lucene
```

## es的核心术语
```
索引 index      -  表
类型 type       -  表逻辑类型
文档 document   -  每一行
字段 fields     -  列的字段

映射mapping     - 表结构定义
近实时          - Near real time
节点            - 每一个服务器
shard          - 数据分片
replica        - 备份
```

## ES集群架构原理
```
ES集群架构原理.png
```

## 倒排索引
```
倒排索引.png
```

## 环境安装
```
install.md
```

## 数据类型
```
text 会分词 会倒排索引
keyword 精确查询 无需分词

没有string 类型了
```