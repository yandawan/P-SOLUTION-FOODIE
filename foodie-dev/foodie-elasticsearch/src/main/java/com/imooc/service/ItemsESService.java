package com.imooc.service;

import com.imooc.model.Items;
import com.imooc.utils.PagedGridResult;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemsESService {

    @Autowired
    private RestHighLevelClient client;

    public PagedGridResult searhItems(String keywords, String sort, Integer page, Integer pageSize) throws IOException {
        SearchRequest searchRequest = new SearchRequest( "foodie-items" );
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchRequest.source(searchSourceBuilder);

        // 匹配关键字
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery( keywords, "itemName" );
        searchSourceBuilder.query( multiMatchQueryBuilder );

        // 排序
        SortBuilder sortBuilder = null;
        if (sort.equals("c")) {
            sortBuilder = new FieldSortBuilder("sellCounts").order(SortOrder.DESC);
        } else if (sort.equals("p")) {
            sortBuilder = new FieldSortBuilder("price").order(SortOrder.ASC);
        } else {
            sortBuilder = new FieldSortBuilder("itemName.keyword").order(SortOrder.ASC);
        }
        searchSourceBuilder.sort(sortBuilder);

        // 高亮设置
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags( "<font color='red'>" );//设置前缀
        highlightBuilder.postTags( "</font>" );//设置后缀
        // 设置高亮字段
        highlightBuilder.fields().add( new HighlightBuilder.Field( "itemName" ) );
        searchSourceBuilder.highlighter( highlightBuilder );

        SearchResponse searchResponse = client.search( searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        List<Items> itemHighLightList = new ArrayList<>();

        for (SearchHit h : searchHits) {
            HighlightField highlightField = h.getHighlightFields().get("itemName");
            String itemName = highlightField.getFragments()[0].toString();

            String itemId = (String)h.getSourceAsMap().get("itemId");
            String imgUrl = (String)h.getSourceAsMap().get("imgUrl");
            Integer price = (Integer)h.getSourceAsMap().get("price");
            Integer sellCounts = (Integer)h.getSourceAsMap().get("sellCounts");

            Items item = new Items();
            item.setItemId(itemId);
            item.setItemName(itemName);
            item.setImgUrl(imgUrl);
            item.setPrice(price);
            item.setSellCounts(sellCounts);

            itemHighLightList.add(item);
        }

        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setRows(itemHighLightList);
        gridResult.setPage(page + 1);
        gridResult.setTotal((int)hits.getTotalHits().value);
        // 需要自己计算
        gridResult.setRecords((int)hits.getTotalHits().value);

        return gridResult;
    }
}
