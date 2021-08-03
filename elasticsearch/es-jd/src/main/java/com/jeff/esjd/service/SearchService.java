package com.jeff.esjd.service;


import com.alibaba.fastjson.JSON;
import com.jeff.esjd.config.ESJDConst;
import com.jeff.vo.JDGoods;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    @Qualifier("restHighLevelClient")
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public List<Map<String, Object>> search(String keyWrod, int pageNo, int pageSize) throws IOException {
        List<Map<String, Object>> result = null;

        //查询构造器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.highlighter();//配置高亮
        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(3));
        //分页
        searchSourceBuilder.from(pageNo);
        searchSourceBuilder.size(pageSize);

        //精准匹配
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", keyWrod);
        searchSourceBuilder.query(termQueryBuilder);


        SearchRequest searchRequest = new SearchRequest(ESJDConst.INDEX);
        searchRequest.source(searchSourceBuilder);
        //执行搜索
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        if (hits != null) {
            result = new ArrayList<>();
            for (SearchHit hit : hits) {
                result.add(hit.getSourceAsMap());
            }
        }


        return result;
    }

    public List<JDGoods> fetchData(String keyWrod) throws IOException {

        Document docment = Jsoup.parse(new URL(ESJDConst.FETCH_URL + keyWrod), 5000);
        Element j_goodsList = docment.getElementById("J_goodsList");
        Elements elements = j_goodsList.getElementsByTag("li");
        ArrayList<JDGoods> jdGoods = new ArrayList<>();

        for (Element element : elements) {
//            "p-price"
            String img = element.getElementsByClass("p-img").eq(0).get(0).getElementsByClass("p-img").get(0).getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = element.getElementsByClass("p-price").eq(0).text();
            String name = element.getElementsByClass("p-name").eq(0).text();
            jdGoods.add(new JDGoods(name,img,price));
        }

        return jdGoods;
    }

    public boolean bulk2ES(List<JDGoods>  jdGoods) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("5s");
        for (int i = 0; i < jdGoods.size(); i++) {
            bulkRequest.add(new IndexRequest(ESJDConst.INDEX).id("00" + i)
                    .source(JSON.toJSONString(jdGoods.get(i)),
                            XContentType.JSON));
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
//            hasFailures 是否存在失败

        return !bulk.hasFailures();
    }

}
