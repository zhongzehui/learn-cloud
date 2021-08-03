package com.jeff.esjd.controller;


import com.alibaba.fastjson.JSON;
import com.jeff.esjd.service.SearchService;
import com.jeff.vo.JDGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("search/{keyword}")
    public String searchGoodInfo(@PathVariable("keyword") String keyword, @RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        List<Map<String, Object>> search = null;
        try {
            search = searchService.search(keyword, pageNo, pageSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(search!=null){
            return JSON.toJSONString(search);
        }else{
            return "failure";
        }
    }
    @GetMapping("fetchData/{keyword}")
    public String fetchData(@PathVariable("keyword")String keyword){
        try {
            List<JDGoods> jdGoods = searchService.fetchData(keyword);
            if (jdGoods!=null&& jdGoods.size()>0) {
                searchService.bulk2ES(jdGoods);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return "done";

    }


}
