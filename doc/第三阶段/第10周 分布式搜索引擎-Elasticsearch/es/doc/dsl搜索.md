## 创建shop索引
```
分片3 副本1
```
## 创建mapping
```
POST
http://127.0.0.1:9200/shop/_mapping

{
	"properties": {
		"id": {
			"type": "long"
		},
		"age": {
			"type": "integer"
		},
		"username": {
			"type": "keyword"
		},
		"nickname": {
			"type": "text",
			"analyzer": "ik_max_word"
		},
		"money": {
			"type": "float"
		},
		"desc": {
			"type": "text",
			"analyzer": "ik_max_word"
		},
		"sex": {
			"type": "byte"
		},
		"birthday": {
			"type": "date"
		},
		"face": {
			"type": "text",
			"index": false
		}
	}
}
```

## 添加数据 11 条
```
POST
http://127.0.0.1:9200/shop/_doc/2002

{
	"id": 2002,
	"age": 18,
	"username": "imoocAmazing",
	"nickname": "慕课网",
	"money": 88.8,
	"desc": "我在慕课网学习java和前端,学习到了很多知识",
	"sex": 0,
	"birthday": "1992-12-24",
	"face": "https://www.imooc.com/static/img/index/logo.png"
}
```

## 查询 - QueryString
```
GET
http://127.0.0.1:9200/shop/_search?q=desc:慕课网
http://127.0.0.1:9200/shop/_search?q=desc:慕课网&q=age:30
```

## dsl 结构化查询
```
POST
http://127.0.0.1:9200/shop/_doc/_search

{
	"query": {
		"match": {
			"desc": "慕课网"
		}
	}
}

POST
http://127.0.0.1:9200/shop/_doc/_search
{
	"query": {
		"exists": {
			"field": "username"
		}
	}
}

# 查询所有
POST
http://127.0.0.1:9200/shop/_doc/_search

{
	"query": {
		"match_all":{
			
		}
	}
}

POST
http://127.0.0.1:9200/shop/_doc/_search

{
	"query": {
		"match_all":{}
	},
	"_source":[
		"id",
		"nickname",
		"age"
	]
}

```

## 分页
```
POST
http://127.0.0.1:9200/shop/_doc/_search

{
	"query": {
		"match_all":{}
	},
	"_source":[
		"id",
		"nickname",
		"age"
	],
	"from": 0,
	"size": 1
}
```

## term 与 match
```
term 不会分词
match 会先进行分词 然后搜索 类似全文索引

POST
http://127.0.0.1:9200/shop/_doc/_search

{
	"query": {
		"term":{
			"desc": "慕课网"
		}
	},
	"_source":[
		"id",
		"nickname",
		"desc"
	]
}

POST
http://127.0.0.1:9200/shop/_doc/_search

{
	"query": {
		"term":{
			"desc": "慕课网"
		}
	},
	"_source":[
		"id",
		"nickname",
		"desc"
	]
}
```

## terms
```
POST
http://127.0.0.1:9200/shop/_doc/_search

{
	"query": {
		"terms":{
			"desc": ["慕课网","学习","骚年"]
		}
	},
	"_source":[
		"id",
		"nickname",
		"desc"
	]
}
```

## match_phrase
```
先对查询字段进行分词 然后查询条件和顺序必须全部包含

POST
http://127.0.0.1:9200/shop/_doc/_search

{
	"query": {
		"match_phrase":{
			"desc": {
				"query": "大学 毕业"
			}
		}
	},
	"_source":[
		"id",
		"nickname",
		"desc"
	]
}

{
	"query": {
		"match_phrase":{
			"desc": {
				"query": "大学 毕业",
				"slop": 3
			}
		}
	},
	"_source":[
		"id",
		"nickname",
		"desc"
	]
}
```

## dsl搜索-match(operator)与ids
```
POST
http://127.0.0.1:9200/shop/_doc/_search

{
	"query": {
		"match":{
			"desc": {
				"query": "慕课学习网",
				"operator": "and"
			}
		}
	},
	"_source":[
		"id",
		"nickname",
		"desc"
	]
}

{
	"query": {
		"match":{
			"desc": {
				"query": "慕课学习网",
				"operator": "or"
			}
		}
	},
	"_source":[
		"id",
		"nickname",
		"desc"
	]
}

{
	"query": {
		"match":{
			"desc": {
				"query": "慕课学习网",
				"minimum_should_match":"60%" 
			}
		}
	},
	"_source":[
		"id",
		"nickname",
		"desc"
	]
}

# ids
POST
http://127.0.0.1:9200/shop/_doc/_search

{
	"query": {
		"ids":{
			"type": "_doc",
			"values": ["1001","1003","1010"]
		}
	},
	"_source":[
		"id",
		"nickname",
		"desc"
	]
}
```

## dsl 搜索 multi_match与boost
```
POST
http://127.0.0.1:9200/shop/_doc/_search

{
	"query": {
		"multi_match":{
			"query": "慕课网",
			"fields": ["desc","nickname"]
		}
	},
	"_source":[
		"id",
		"nickname",
		"desc"
	]
}
```

## boost
```
# 分数的提升
POST
http://127.0.0.1:9200/shop/_doc/_search

{
	"query": {
		"multi_match":{
			"query": "慕课网",
			"fields": ["desc","nickname^10"]
		}
	},
	"_source":[
		"id",
		"nickname",
		"desc"
	]
}
```

## boolean 查询
```
POST
http://127.0.0.1:9200/shop/_doc/_search

{
	"query": {
		"bool":{
			"must": [
			  {
			  	"multi_match":{
			  		"query": "慕课网",
			  		"fields": ["desc","nickname"]
			  	}
			  },
			  {
			  	"term":{
			  		"sex": 0
			  	}
			  }
			]
		}
	},
	"_source":[
		"id",
		"sex",
		"nickname",
		"desc"
	]
}

# must 与
{
	"query": {
		"bool":{
			"must": [
			  {
			  	"multi_match":{
			  		"query": "慕课网",
			  		"fields": ["desc","nickname"]
			  	}
			  },
			  {
			  	"term":{
			  		"sex": 0
			  	}
			  },
			  {
			  	"term":{
			  		"birthday": "1992-12-24"
			  	}
			  }
			]
		}
	},
	"_source":[
		"id",
		"sex",
		"nickname",
		"desc"
	]
}


# should 或
{
	"query": {
		"bool":{
			"should": [
			  {
			  	"multi_match":{
			  		"query": "慕课网",
			  		"fields": ["desc","nickname"]
			  	}
			  },
			  {
			  	"term":{
			  		"sex": 0
			  	}
			  },
			  {
			  	"term":{
			  		"birthday": "1992-12-24"
			  	}
			  }
			]
		}
	},
	"_source":[
		"id",
		"sex",
		"nickname",
		"desc"
	]
}

# sholud not
{
	"query": {
		"bool":{
			"must_not": [
			  {
			  	"multi_match":{
			  		"query": "慕课网",
			  		"fields": ["desc","nickname"]
			  	}
			  },
			  {
			  	"term":{
			  		"sex": 0
			  	}
			  },
			  {
			  	"term":{
			  		"birthday": "1992-12-24"
			  	}
			  }
			]
		}
	},
	"_source":[
		"id",
		"sex",
		"nickname",
		"desc"
	]
}

# 条件组合
{
	"query": {
		"bool":{
			"must": [
			  {
			  	"match":{
			  		"desc": "慕",
			  		"boost": 2
			  	}
			  },
			  {
			  	"match":{
			  		"nickname": "慕",
			  		"boost": 10
			  	}
			  }
			],
			"should":[
				{
					"match":{
						"sex": 1
					}
				}
			],
			"must_not": [
				{
					"term":{
						"birthday": "1992-12-24"
					}
				}
			]
		}
	},
	"_source":[
		"id",
		"sex",
		"nickname",
		"desc"
	]
}
```

## 过滤器 post_filter
```
POST
http://127.0.0.1:9200/shop/_doc/_search

{
	"query": {
		"match":{
	  		"desc": "慕"
	  	}
	},
	"post_filter": {
		"range": {
			"money": {
				"gt": 60,
				"lt": 1000
			}
		}
	}
}

POST
http://127.0.0.1:9200/shop/_doc/_search

{
	"query": {
		"match":{
	  		"desc": "慕"
	  	}
	},
	"post_filter": {
		"term": {
			"birthday": "1992"
		}
	}
}
```

## 排序
```
POST
http://127.0.0.1:9200/shop/_doc/_search

{
	"query": {
		"match":{
	  		"desc": "慕"
	  	}
	},
    "sort": [
   	  {
   	  	"age": "desc"
   	  }
	]
}

POST
http://127.0.0.1:9200/shop/_doc/_search

{
	"query": {
		"match":{
	  		"desc": "慕"
	  	}
	},
    "sort": [
      {
   	  	"money": "asc"
   	  },	
   	  {
   	  	"age": "desc"
   	  }
	]
}
```

## 新建索引
```
POST
http://127.0.0.1:9200/shop2/_mapping

{
	"properties":{
		"id":{
			"type": "long"
		},
		"nickname":{
			"type":"text",
			"analyzer": "ik_max_word",
			"fields":{
				"keyword": {
					"type": "keyword"
				}
			}
		}
	}
}


```

## 高亮
```
POST
http://127.0.0.1:9200/shop/_doc/_search

{
	"query": {
		"match": {
			"desc": "慕课网"
		}
	},
	"highlight":{
		"fields":{
			"desc": {}
		}
	}
}

# 高亮自定义标签
POST
http://127.0.0.1:9200/shop/_doc/_search


{
	"query": {
		"match": {
			"desc": "慕课网"
		}
	},
	"highlight":{
		"fields":{
			"desc": {}
		}
	}
}

{
	"query": {
		"match": {
			"desc": "慕课网"
		}
	},
	"highlight":{
		"pre_tags": ["<span>"],
		"post_tags": ["</span>"],
		"fields":{
			"desc": {}
		}
	}
}
```



