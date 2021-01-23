# Scroll滚动搜索

## 滚动搜索
```
# 查询的初始化
POST
http://127.0.0.1:9200/shop/_search?scroll=1m

{
	"query": {
		"match_all": {}
	},
	"sort": ["_doc"],
	"size": 1000
}

# 下一次查询
POST
http://127.0.0.1:9200/_search/scroll

{
	"scroll_id": "DnF1ZXJ5VGhlbkZldGNoAwAAAAAAAAFkFnVxbl9OUzdXUWV5N3dBcF9EV2lVeUEAAAAAAAABZRZ1cW5fTlM3V1FleTd3QXBfRFdpVXlBAAAAAAAAAWYWdXFuX05TN1dRZXk3d0FwX0RXaVV5QQ==",
    "scroll": "1m"
}
```