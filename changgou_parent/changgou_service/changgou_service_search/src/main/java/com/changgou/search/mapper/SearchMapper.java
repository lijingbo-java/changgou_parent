package com.changgou.search.mapper;

import com.changgou.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/21 23:32
 * @description ：
 * @version: 1.0
 */
public interface SearchMapper extends ElasticsearchRepository<SkuInfo,Long> {
}
