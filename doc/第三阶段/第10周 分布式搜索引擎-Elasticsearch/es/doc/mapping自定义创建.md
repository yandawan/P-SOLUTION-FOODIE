# mapping自定义创建

## 创建
```
PUT
http://127.0.0.1:9200/index_mapping

{
	"mappings":{
		"properties":{
			"realname":{
				"type": "text",
				"index": true
			},
			"username":{
				"type":"keyword",
				"index": false
			}
		}
	}
}
```

## 分词
```
1 "index": true 有分词
POST
http://127.0.0.1:9200/index_mapping/_analyze

{
	"field": "realname",
	"text": "imooc is very good~!"
}

返回结果: 有分词

2 "index": false 没有分词
POST
http://127.0.0.1:9200/index_mapping/_analyze

{
	"field": "username",
	"text": "imooc is very good~!"
}

返回结果: 没有分词
```

## 修改分词
```
POST
http://127.0.0.1:9200/index_mapping/_mapping

{
	"properties":{
		"id": {
			"type": "long"
		},
		"age":{
			"type": "integer"
		}
	}
}

{
	"properties":{
		"money1": {
			"type": "double"
		},
		"money2":{
			"type": "float"
		}
	}
}

{
	"properties":{
		"sex": {
			"type": "byte"
		},
		"score":{
			"type": "short"
		}
	}
}

{
	"properties":{
		"is_teenger": {
			"type": "boolean"
		},
		"birthday":{
			"type": "date"
		},
		"relationship":{
			"type": "object"
		}
	}
}
```