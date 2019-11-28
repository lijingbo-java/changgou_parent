package com.changgou.search.service;

import java.util.Map;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/22 9:16
 * @description ：ES搜索业务接口
 * @version: 1.0
 */
public interface SearchService {

    void importData();

    Map search(Map<String,String> searchMap);
}
