package com.changgou.search.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/21 11:39
 * @description ：搜索控制器
 * @version: 1.0
 */
@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired//生成骨架,导入数据
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private SearchService searchService;




    /**
     * 根据多个条件查询
     *
     * @param searchMap
     * @return
     */
    @GetMapping
    public Map search(@RequestParam Map<String, String> searchMap) {
        return searchService.search(searchMap);
    }


    @RequestMapping("/importData")
    public Result importData() {
        searchService.importData();
        return new Result(true, StatusCode.OK, "导入数据成功");
    }

}
