# ik 中文分词器

## 细粒度拆分
```
POST
http://127.0.0.1:9200/_analyze

{
	"analyzer": "ik_max_word",
	"text": "上下班车流量很大"
}


```

## 粗粒度拆分
```
POST
http://127.0.0.1:9200/_analyze

{
	"analyzer": "ik_smart",
	"text": "上下班车流量很大"
}


```