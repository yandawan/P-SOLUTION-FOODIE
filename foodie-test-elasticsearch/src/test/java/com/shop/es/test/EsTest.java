package com.shop.es.test;

import com.alibaba.fastjson.JSON;
import com.shop.es.ESApplication;
import com.shop.es.util.ElasticUtil;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ESApplication.class)
public class EsTest {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ElasticUtil elasticUtil;

    /**
     * 创建索引 和 映射
     *
     * @throws IOException
     */
    @Test
    public void createIndex() throws IOException {

        //创建名称为blog2的索
        CreateIndexRequest request = new CreateIndexRequest( "blog1" );

        //设置映射 doc type名称
        request.mapping( "doc", " {\n" +
                " \t\"properties\": {\n" +
                "           \"name\": {\n" +
                "              \"type\": \"text\",\n" +
                "              \"analyzer\":\"ik_max_word\",\n" +
                "              \"search_analyzer\":\"ik_smart\"\n" +
                "           },\n" +
                "           \"description\": {\n" +
                "              \"type\": \"text\",\n" +
                "              \"analyzer\":\"ik_max_word\",\n" +
                "              \"search_analyzer\":\"ik_smart\"\n" +
                "           },\n" +
                "           \"studymodel\": {\n" +
                "              \"type\": \"keyword\"\n" +
                "           },\n" +
                "           \"price\": {\n" +
                "              \"type\": \"float\"\n" +
                "           }\n" +
                "        }\n" +
                "}", XContentType.JSON );
        CreateIndexResponse createIndexResponse = client.indices().create( request, RequestOptions.DEFAULT);
        System.out.println( JSON.toJSONString( createIndexResponse ) );

        //释放资源
        client.close();
    }

    /**
     * 删除索引
     */
    @Test
    public void testDeleteIndex() throws IOException {
        //删除索引请求对象
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest( "blog1" );
        //删除索引
        AcknowledgedResponse deleteIndexResponse = client.indices().delete( deleteIndexRequest, RequestOptions.DEFAULT);
        //删除索引响应结果
        boolean acknowledged = deleteIndexResponse.isAcknowledged();
        System.out.println( acknowledged );
    }

    /**
     * 插入文档
     */
    @Test
    public void testAddDocument() throws IOException {
        //准备json数据
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put( "name", "22spring cloud实战" );
        jsonMap.put( "description", "11本课程主要从四个章节进行讲解： 1.微服务架构入门 2.spring cloud基础入门 3.实战Spring Boot 4.注册中心eureka。" );
        jsonMap.put( "studymodel", "201001" );
        jsonMap.put( "price", 5.6f );

        //索引请求对象
        IndexRequest indexRequest = new IndexRequest( "blog1", "doc" ).id( "11" );
        //指定索引文档内容
        indexRequest.source( jsonMap );

        //索引响应对象
        IndexResponse index = client.index( indexRequest, RequestOptions.DEFAULT);

        //获取响应结果
        DocWriteResponse.Result result = index.getResult();
        System.out.println( result );
    }

    /**
     * 更新文档
     */
    @Test
    public void updateDocument() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest( "blog1", "doc", "11" );

        Map<String, String> map = new HashMap<>();
        map.put( "name", "spring cloud实战222" );
        updateRequest.doc( JSON.toJSONString(map),XContentType.JSON);

        UpdateResponse update = client.update( updateRequest, RequestOptions.DEFAULT);

        RestStatus status = update.status();

        System.out.println( status );

    }

    /**
     * 删除文档
     */
    @Test
    public void deleteDocument() throws IOException {
        String id = "AXIDNJoC19Z06cvw7_Gv";
        DeleteRequest deleteRequest = new DeleteRequest( "blog2", "doc", id );

        DeleteResponse delete = client.delete( deleteRequest, RequestOptions.DEFAULT);

        System.out.println( delete.status() );
    }

    /**
     * 查询文档  根据ID查询
     */
    @Test
    public void getDocumentById() throws IOException {
        GetRequest getRequest = new GetRequest( "blog1", "doc", "11" );

        GetResponse response = client.get( getRequest , RequestOptions.DEFAULT);

        boolean exists = response.isExists();

        Map<String, Object> sourceAsMap = response.getSourceAsMap();
        System.out.println( sourceAsMap );

    }

    /**
     * 搜索管理  查询所有文档
     */
    @Test
    public void testSearchAll() throws IOException {

        SearchRequest searchRequest = new SearchRequest( "blog1" );
        searchRequest.types( "doc" );
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query( QueryBuilders.matchAllQuery() );
        //source源字段过虑
        searchSourceBuilder.fetchSource( new String[]{"name", "studymodel", "description"}, new String[]{} );
        searchRequest.source( searchSourceBuilder );
        SearchResponse searchResponse = client.search( searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        TotalHits totalHits = hits.getTotalHits();

        System.out.println( "total=" + totalHits );

        SearchHit[] searchHits = hits.getHits();

        for (SearchHit searchHit : searchHits) {
            String index = searchHit.getIndex();
            System.out.println( "index=" + index );
            String id = searchHit.getId();
            System.out.println( "id=" + id );
            String sourceAsString = searchHit.getSourceAsString();
            System.out.println( sourceAsString );
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            // String articleId = String.valueOf( sourceAsMap.get( "id" ) );
            String title = (String) sourceAsMap.get( "name" );
            String content = (String) sourceAsMap.get( "description" );
            // System.out.println("articleId="+articleId);
            System.out.println( "title=" + title );
            System.out.println( "content=" + content );
        }
    }

    /**
     * 搜索管理  分页查询
     */
    @Test
    public void testSearchByPage() throws IOException {
        //设置要查询的索引 和 type
        SearchRequest searchRequest = new SearchRequest( "blog1" );
        searchRequest.types( "doc" );
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query( QueryBuilders.matchAllQuery() );
        //分页查询，设置起始下标，从0开始
        searchSourceBuilder.from( 0 );
        //每页显示个数
        searchSourceBuilder.size( 2 );
        //source源字段过虑
        //searchSourceBuilder.fetchSource(new String[]{"title","id","content"},new String[]{}  );
        searchSourceBuilder.fetchSource( new String[]{"name", "studymodel", "description"}, new String[]{} );
        searchRequest.source( searchSourceBuilder );


        SearchResponse search = client.search( searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        TotalHits totalHits = hits.getTotalHits();
        System.out.println( totalHits );
        for (SearchHit searchHit : hits.getHits()) {
            String sourceAsString = searchHit.getSourceAsString();
            System.out.println( sourceAsString );
        }
    }

    /**
     * 搜索管理  Term Query精确查找 ，在搜索时会整体匹配关键字，不再将关键字分词 ，
     * 下面的语句：查询title 包含 spring 的文档
     */
    @Test
    public void testSearchTerm() throws IOException {
        //创建查询，设置索引
        SearchRequest searchRequest = new SearchRequest( "blog1" );
        //设置type
        searchRequest.types( "doc" );
        //设置查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query( QueryBuilders.termQuery( "name", "spring" ) );
        //source源字段过虑
        //searchSourceBuilder.fetchSource(new String[]{"title","id","content"},new String[]{});
        searchSourceBuilder.fetchSource( new String[]{"name", "studymodel", "description"}, new String[]{} );
        searchRequest.source( searchSourceBuilder );

        SearchResponse search = client.search( searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        TotalHits totalHits = hits.getTotalHits();
        System.out.println( "总条数：" + totalHits );

        for (SearchHit searchHit : hits.getHits()) {
            String sourceAsString = searchHit.getSourceAsString();
            System.out.println( sourceAsString );
        }
    }

    /**
     * 搜索管理 根据ID查询
     */
    @Test
    public void testSearchByID() throws IOException {
        //创建查询，设置索引
        SearchRequest searchRequest = new SearchRequest( "blog1" );
        //设置type
        searchRequest.types( "doc" );
        //设置查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        String[] ids = {"11", "12"};
        List<String> stringList = Arrays.asList( ids );
        searchSourceBuilder.query( QueryBuilders.termsQuery( "_id", stringList ) );
        //source源字段过虑
        searchSourceBuilder.fetchSource( new String[]{"name", "studymodel", "description"}, new String[]{} );
        searchRequest.source( searchSourceBuilder );

        SearchResponse search = client.search( searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        TotalHits totalHits = hits.getTotalHits();
        System.out.println( "总条数：" + totalHits );

        for (SearchHit searchHit : hits.getHits()) {
            String sourceAsString = searchHit.getSourceAsString();
            System.out.println( sourceAsString );
        }
    }

    /**
     * 搜索管理 match query 先分词后查找 minimum_should_match  （如果实现三个词至少有两个词匹配如何实现）
     */
    @Test
    public void testSearchMatchMinimnum() throws IOException {
        //创建查询，设置索引
        SearchRequest searchRequest = new SearchRequest( "blog1" );
        //设置type
        searchRequest.types( "doc" );
        //设置查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //匹配关键字
        searchSourceBuilder.query( QueryBuilders.matchQuery( "name", "spring开发" ).minimumShouldMatch( "60%" ) );
        //source源字段过虑
        searchSourceBuilder.fetchSource( new String[]{"name", "studymodel", "description"}, new String[]{} );
        searchRequest.source( searchSourceBuilder );

        SearchResponse search = client.search( searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        TotalHits totalHits = hits.getTotalHits();
        System.out.println( "总条数：" + totalHits );

        for (SearchHit searchHit : hits.getHits()) {
            String sourceAsString = searchHit.getSourceAsString();
            System.out.println( sourceAsString );
        }
    }

    /**
     * 搜索管理 同时搜索多个Field
     */
    @Test
    public void testSearchMultiMatch() throws IOException {
        //创建查询，设置索引
        SearchRequest searchRequest = new SearchRequest( "blog1" );
        //设置type
        searchRequest.types( "doc" );
        //设置查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //匹配关键字
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery( "Boot 开发", "name", "description" )
                .minimumShouldMatch( "50%" );
        multiMatchQueryBuilder.field( "name", 10 );//提升boost 表示权重提升10倍

        searchSourceBuilder.query( multiMatchQueryBuilder );
        //source源字段过虑
        searchSourceBuilder.fetchSource( new String[]{"name", "studymodel", "description"}, new String[]{} );
        searchRequest.source( searchSourceBuilder );

        SearchResponse search = client.search( searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        TotalHits totalHits = hits.getTotalHits();
        System.out.println( "总条数：" + totalHits );

        for (SearchHit searchHit : hits.getHits()) {
            String sourceAsString = searchHit.getSourceAsString();
            System.out.println( sourceAsString );
        }
    }

    /**
     * 搜索管理 布尔查询
     */
    @Test
    public void testSearchBoolean() throws IOException {
        //创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest( "blog1" );
        //设置type
        searchRequest.types( "doc" );
        //创建搜索源配置对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //source源字段过虑
        searchSourceBuilder.fetchSource( new String[]{"name", "studymodel", "description"}, new String[]{} );
        //multiQuery
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery( "Boot 开发", "name", "description" )
                .minimumShouldMatch( "50%" );
        multiMatchQueryBuilder.field( "name", 10 );//提升boost 表示权重提升10倍
        //TermQuery
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery( "studymodel", "201001" );
        // 布尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must( multiMatchQueryBuilder );
        boolQueryBuilder.must( termQueryBuilder );
        //设置布尔查询对象
        searchSourceBuilder.query( boolQueryBuilder );
        //设置搜索源配置
        searchRequest.source( searchSourceBuilder );
        //搜索
        SearchResponse search = client.search( searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        TotalHits totalHits = hits.getTotalHits();
        System.out.println( "总条数：" + totalHits );

        for (SearchHit searchHit : hits.getHits()) {
            String sourceAsString = searchHit.getSourceAsString();
            System.out.println( sourceAsString );
        }
    }

    /**
     * 搜索管理 过滤器
     */
    @Test
    public void testSearchFilter() throws IOException {
        //创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest( "blog1" );
        //设置type
        searchRequest.types( "doc" );
        //创建搜索源配置对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //source源字段过虑
        searchSourceBuilder.fetchSource( new String[]{"name", "studymodel", "description"}, new String[]{} );
        //multiQuery 多field查询
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery( "Boot 开发", "name", "description" )
                .minimumShouldMatch( "50%" );
        multiMatchQueryBuilder.field( "name", 10 );//提升boost 表示权重提升10倍
        //多Field查询，添加到搜索源配置对象中
        searchSourceBuilder.query( multiMatchQueryBuilder );

        //TermQuery
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery( "studymodel", "201001" );
        //Term查询，添加到搜索源配置对象中
        searchSourceBuilder.query( termQueryBuilder );

        // 布尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must( searchSourceBuilder.query() );

        //过虑
        boolQueryBuilder.filter( QueryBuilders.termQuery( "studymodel", "201001" ) );
        boolQueryBuilder.filter( QueryBuilders.rangeQuery( "price" ).gte( 5 ).lte( 100 ) );

        //设置布尔查询对象
        searchSourceBuilder.query( boolQueryBuilder );
        //设置搜索源配置
        searchRequest.source( searchSourceBuilder );
        //搜索
        SearchResponse search = client.search( searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        TotalHits totalHits = hits.getTotalHits();
        System.out.println( "总条数：" + totalHits );

        for (SearchHit searchHit : hits.getHits()) {
            String sourceAsString = searchHit.getSourceAsString();
            System.out.println( sourceAsString );
        }
    }

    /**
     * 排序
     *
     * @throws IOException
     */
    @Test
    public void testSort() throws IOException {
        SearchRequest searchRequest = new SearchRequest( "blog1" );
        searchRequest.types( "doc" );
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //source源字段过虑
        searchSourceBuilder.fetchSource( new String[]{"name", "studymodel", "price", "description"}, new String[]{} );
        searchRequest.source( searchSourceBuilder );
        //布尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //过虑
        boolQueryBuilder.filter( QueryBuilders.rangeQuery( "price" ).gte( 0 ).lte( 100 ) );
        // 排序
        searchSourceBuilder.sort( new FieldSortBuilder( "studymodel" ).order( SortOrder.DESC ) );
        searchSourceBuilder.sort( new FieldSortBuilder( "price" ).order( SortOrder.ASC ) );
        //搜索
        SearchResponse searchResponse = client.search( searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            String sourceAsString = hit.getSourceAsString();
            System.out.println( sourceAsString );
        }

    }

    /**
     * 高亮显示
     *
     * @throws IOException
     */
    @Test
    public void testHighlight() throws IOException {
        SearchRequest searchRequest = new SearchRequest( "blog1" );
        searchRequest.types( "doc" );

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //source源字段过虑
        searchSourceBuilder.fetchSource( new String[]{"name", "studymodel", "price", "description"}, new String[]{} );
        searchRequest.source( searchSourceBuilder );
        //匹配关键字
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery( "实战", "name", "description" );
        searchSourceBuilder.query( multiMatchQueryBuilder );
        //布尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must( searchSourceBuilder.query() );
        //过虑
        boolQueryBuilder.filter( QueryBuilders.rangeQuery( "price" ).gte( 0 ).lte( 100 ) );
        //排序
        searchSourceBuilder.sort( new FieldSortBuilder( "studymodel" ).order( SortOrder.DESC ) );
        searchSourceBuilder.sort( new FieldSortBuilder( "price" ).order( SortOrder.ASC ) );
        //高亮设置
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags( "<tag>" );//设置前缀
        highlightBuilder.postTags( "</tag>" );//设置后缀
        // 设置高亮字段
        highlightBuilder.fields().add( new HighlightBuilder.Field( "name" ) );
        //highlightBuilder.fields().add(new HighlightBuilder.Field("description"));
        searchSourceBuilder.highlighter( highlightBuilder );

        SearchResponse searchResponse = client.search( searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            //名称
            String name = (String) sourceAsMap.get( "name" );
            //取出高亮字段内容
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields != null) {
                HighlightField nameField = highlightFields.get( "name" );
                if (nameField != null) {
                    Text[] fragments = nameField.getFragments();
                    StringBuffer stringBuffer = new StringBuffer();
                    for (Text str : fragments) {
                        stringBuffer.append( str.string() );
                    }
                    name = stringBuffer.toString();
                }
            }
            System.out.println( "高亮==" + name );

            String sourceAsString = hit.getSourceAsString();
            System.out.println( sourceAsString );
        }
    }


//    @Testa
//    public void indexExists() {
//        boolean exists = elsTemplate.indexExists(BookBean.class);
//        System.out.println(exists);
//    }
//
//    @Test
//    public void deleteIndex() {
//        DeleteIndexRequest request = new DeleteIndexRequest("twitter");
//        try {
//            AcknowledgedResponse acknowledgedResponse =client.indices().delete(request, RequestOptions.DEFAULT);
//            System.out.println(acknowledgedResponse);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * map 方式
//     */
//    @Test
//    public void add(){
//        Map<String, Object> jsonMap = new HashMap<>();
//        jsonMap.put("user", "kimchy");
//        jsonMap.put("postDate", new Date());
//        jsonMap.put("message", "trying out Elasticsearch");
//        IndexRequest indexRequest = new IndexRequest("twitter").id("1").source(jsonMap);
//        try {
//            client.index(indexRequest, RequestOptions.DEFAULT);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 利用阿里巴巴 JSON库  将对象转化为 json 插入
//     */
//    @Test
//    public void add1(){
//
//        Twitter twitter = new Twitter();
//        twitter.setId(2L);
//        twitter.setMessage("hello twitter!");
//        twitter.setPostDate(new Date());
//        twitter.setUser("kimchy");
//
//        IndexRequest request = new IndexRequest("twitter");
//        request.id(String.valueOf(twitter.getId()));
//        request.source(JSON.toJSONString(twitter), XContentType.JSON);
//        try {
//            client.index(request, RequestOptions.DEFAULT);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     * 利用XContentBuilder 方式插入
//     * @throws IOException
//     */
//    @Test
//    public void add2() throws IOException {
//        XContentBuilder builder = XContentFactory.jsonBuilder();
//        builder.startObject();
//        {
//            builder.field("user", "kimchy");
//            builder.timeField("postDate", new Date());
//            builder.field("message", "trying out Elasticsearch");
//        }
//        builder.endObject();
//        IndexRequest indexRequest = new IndexRequest("twitter")
//                .id("3").source(builder);
//
//        try {
//            client.index(indexRequest, RequestOptions.DEFAULT);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     *  UpdateRequest 第一个参数是 索引，第二个参数是 数据主键id ，表示修改id 为1 的 twitter 数据
//     */
//    @Test
//    public void update(){
//        Map<String, Object> jsonMap = new HashMap<>();
//        jsonMap.put("user", "kimchy1");
//        jsonMap.put("postDate", new Date());
//        jsonMap.put("message", "trying update Elasticsearch");
//        UpdateRequest request = new UpdateRequest("twitter", "1").doc(jsonMap);
//        try {
//            UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
//            System.out.println(updateResponse);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//    @Test
//    public void update1(){
//        Twitter twitter = new Twitter();
//        twitter.setId(1L);
//        twitter.setMessage("hello twitter update!");
//        twitter.setPostDate(new Date());
//        twitter.setUser("kimchy");
//
//        UpdateRequest request = new UpdateRequest("twitter", String.valueOf(twitter.getId()))
//                .doc(JSON.toJSONString(twitter), XContentType.JSON);
//
//        try {
//            UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
//            System.out.println(updateResponse);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Test
//    public void update2() throws IOException {
//        XContentBuilder builder = XContentFactory.jsonBuilder();
//        builder.startObject();
//        {
//            builder.field("user", "kimchy");
//            builder.timeField("postDate", new Date());
//            builder.field("message", "trying update Elasticsearch");
//        }
//        builder.endObject();
//        UpdateRequest request = new UpdateRequest("twitter", "1")
//                .doc(builder);
//
//        try {
//            UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
//            System.out.println(updateResponse);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 删除id 为1 的数据
//     */
//    @Test
//    public void deleteById() {
//
//        DeleteRequest request = new DeleteRequest("twitter", "1");
//        try {
//            DeleteResponse deleteResponse = client.delete(request, RequestOptions.DEFAULT);
//
//            System.out.println(deleteResponse);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     * 获取id 为1 的数据
//     */
//    @Test
//    public void getById(){
//        GetRequest getRequest = new GetRequest("twitter", "1");
//        GetResponse getResponse = null;
//        try {
//            getResponse = client.get(getRequest, RequestOptions.DEFAULT);
//            if (getResponse.isExists()) {
//
//                Map<String,Object> map = getResponse.getSourceAsMap();
//
//                Twitter twitter=JSON.parseObject(JSON.toJSONString(map), Twitter.class);
//
//                System.out.println(twitter);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Test
//    public void search(){
//        SearchRequest searchRequest = new SearchRequest("twitter");
//
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//
//        //默认分词查询
//        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("user", "你好企业")
//                .fuzziness(Fuzziness.AUTO) //模糊查询
//                .prefixLength(3) // 在匹配查询上设置前缀长度选项,指明区分词项的共同前缀长度，默认是0
//                .maxExpansions(10); //设置最大扩展选项以控制查询的模糊过程
//
//        //查询条件 添加，user = kimchy
//        //sourceBuilder.query(QueryBuilders.termQuery("user", "kimchy"));
//        sourceBuilder.query(matchQueryBuilder);
//
//
//        //查询开始-结束 。可以用来分页使用
//        sourceBuilder.from(0);
//        sourceBuilder.size(5);
//
//        //设置一个可选的超时，控制允许搜索的时间。
//        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
//
//        //排序
//        sourceBuilder.sort(new FieldSortBuilder("id").order(SortOrder.ASC));
//
//        searchRequest.source(sourceBuilder);
//
//        try {
//            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//            //处理搜索结果
//            RestStatus restStatus = searchResponse.status();
//            if (restStatus != RestStatus.OK){
//                System.out.println("搜索错误");
//            }
//            List<Twitter> list = new ArrayList<>();
//            SearchHits hits = searchResponse.getHits();
//            hits.forEach(item -> list.add(JSON.parseObject(item.getSourceAsString(), Twitter.class)));
//            System.out.println(list);
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     * 高亮搜索
//     * @param indexName 索引名称
//     * @param queryBuilder 查询条件
//     * @param highligtFiled 高亮字段
//     * @return
//     */
//    public SearchResponse searcherHighlight(String indexName,QueryBuilder queryBuilder, String highligtFiled) {
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();//构造搜索对象
//        searchSourceBuilder.query(queryBuilder);//设置查询条件
//        //设置高亮
//        String preTags = "<strong>";
//        String postTags = "</strong>";
//        HighlightBuilder highlightBuilder = new HighlightBuilder();
//        highlightBuilder.preTags(preTags);//设置前缀
//        highlightBuilder.postTags(postTags);//设置后缀
//        highlightBuilder.field(highligtFiled);//设置高亮字段
//        searchSourceBuilder.highlighter(highlightBuilder);//设置高亮信息
//
//        SearchRequest searchRequest = new SearchRequest(indexName);//创建查询请求对象
//        searchRequest.source(searchSourceBuilder);//设置searchSourceBuilder
//
//        SearchResponse searchResponse = null;//执行查询
//        try {
//            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return searchResponse;
//    }
//
//
//
//    @Test
//    public void highlighting(){
//        String indexName = "twitter";//索引名称
//        String highligtFiled = "message";//设置高亮的字段，此处查询的是interest中含有basketball的文档，因此高亮字段设为interest
//        QueryBuilder queryBuilder = QueryBuilders.matchQuery("message",
//                "Elasticsearch");//查询message中含有Elasticsearch的文档
//        SearchResponse searchResponse = searcherHighlight(indexName,
//                queryBuilder, highligtFiled);
//
//        //处理搜索结果
//        RestStatus restStatus = searchResponse.status();
//        if (restStatus != RestStatus.OK){
//            System.out.println("搜索错误");
//        }
//        List<Twitter> list = new ArrayList<>();
//        SearchHits hits = searchResponse.getHits();
//        hits.forEach(item -> {
//                    Twitter twitter = JSON.parseObject(item.getSourceAsString(), Twitter.class);
//                    Map<String, HighlightField> map = item.getHighlightFields()  ;
//                    System.out.println(map.toString());
//                    twitter.setHighlight(map);
//                    list.add(twitter);
//                }
//        );
//        System.out.println(list);
//
//    }
//
//

}