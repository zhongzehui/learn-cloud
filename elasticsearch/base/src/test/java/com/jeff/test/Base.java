package com.jeff.test;

import com.alibaba.fastjson.JSON;
import com.jeff.elasticsearch.BaseApplication;
import com.jeff.vo.Person;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
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
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContent;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BaseApplication.class})
public class Base {

    @Qualifier("restHighLevelClient")
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void init() {
        if (restHighLevelClient != null) {
            System.out.println(restHighLevelClient.toString());
            Person person = new Person(123, "张三", "zhang3");
            IndexRequest indexRequest = new IndexRequest();
//            restHighLevelClient.indices().
        }
    }

    public static final String INDEX = "es-index-001";

    @Test
    public void creatIndex() throws IOException {
        if (restHighLevelClient != null) {
            System.out.println(restHighLevelClient.toString());
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(INDEX);
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            System.out.println(createIndexRequest.toString());
        }
    }

    @Test
    public void existsIndex() throws IOException {
        if (restHighLevelClient != null) {
            System.out.println(restHighLevelClient.toString());
            GetIndexRequest getRequest = new GetIndexRequest(INDEX);
            boolean exists = restHighLevelClient.indices()
                    .exists(getRequest, RequestOptions.DEFAULT);
            System.out.println("判断索引是否存在===》" + exists);
        }
    }

    @Test
    public void deleteIndex() throws IOException {
        if (restHighLevelClient != null) {
            System.out.println(restHighLevelClient.toString());
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(INDEX);
            AcknowledgedResponse delete = restHighLevelClient.indices()
                    .delete(deleteIndexRequest, RequestOptions.DEFAULT);
            System.out.println("判断索引是否删除成功===》" + JSON.toJSONString(delete));
        }
    }

    @Test
    public void addDoc() throws IOException {
        if (restHighLevelClient != null) {
            //添加文档
            System.out.println(restHighLevelClient.toString());
            Person person = new Person(123, "张三", "zhang3");
            IndexRequest indexRequest = new IndexRequest(INDEX);
            indexRequest.id(person.getId().toString());
            indexRequest.timeout(TimeValue.timeValueSeconds(1));
            indexRequest.timeout("1s");
            indexRequest.source(JSON.toJSONString(person), XContentType.JSON);
            IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            System.out.println("判断索引是否添加文档成功===》" + JSON.toJSONString(index));
        }
    }

    @Test
    public void getDocInfo() throws IOException {
        if (restHighLevelClient != null) {
            //添加文档
            GetRequest getRequest = new GetRequest(INDEX);
            getRequest.id("123");
            //判断是否存在
            boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
            if (exists) {
                System.out.println("存在该数据!!");
            } else {
                System.out.println("不存在该数据!!");
            }


            GetResponse documentFields = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            String sourceAsString = documentFields.getSourceAsString();
            //sourceAsString这个就是文档内容！
            System.out.println("判断索引是否添加文档成功===》" + JSON.toJSONString(documentFields));
        }
    }

    @Test
    public void testUpdate() throws IOException {
        if (restHighLevelClient != null) {
            //添加文档
            UpdateRequest updateRequest = new UpdateRequest(INDEX, "123");
            updateRequest.timeout("1s");
            Person person = new Person(456, "张三", "zhang three");
            updateRequest.doc(JSON.toJSONString(person), XContentType.JSON);
            UpdateResponse update = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            System.out.println("判断索引是否更新文档成功===》" + JSON.toJSONString(update));
        }
    }

    @Test
    public void testBuilRequest() throws IOException {
        //批量操作
        if (restHighLevelClient != null) {
            BulkRequest bulkRequest = new BulkRequest();
            bulkRequest.timeout("5s");
            List<Person> personList = new ArrayList<>();
            personList.add(new Person(451, "张三2", "zhang1 three"));
            personList.add(new Person(452, "张三1", "zhangw three"));
            personList.add(new Person(453, "张三3", "zhangsw three"));
            personList.add(new Person(454, "张三4", "zhanga three"));
            personList.add(new Person(457, "张三5", "zhangzz three"));

            for (int i = 0; i < personList.size(); i++) {
                bulkRequest.add(new IndexRequest(INDEX).id("00" + i)
                        .source(JSON.toJSONString(personList.get(i)),
                                XContentType.JSON));
            }
            BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
//            hasFailures 是否存在失败
            System.out.println("批量新增文档结果===》" + bulk.hasFailures());

        }
    }


    @Test
    public void Search() {
        if (restHighLevelClient != null) {
            SearchRequest searchRequest = new SearchRequest(INDEX);
//            构造搜索条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            //查询条件可以用QueryBuilders构造查询条件
//            QueryBuilders.termQuery()精确查询
//            QueryBuilders.matchAllQuery();匹配所有

            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "张三2");

            searchSourceBuilder.query(termQueryBuilder);

            searchSourceBuilder.timeout(new TimeValue(3000));

            searchSourceBuilder.highlighter();

//            searchSourceBuilder.from()分页
            searchRequest.source(searchSourceBuilder);
            try {
                SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
                SearchHits hits = search.getHits();
                for (SearchHit hit : hits) {
                    System.out.println(hit.getSourceAsMap().toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
