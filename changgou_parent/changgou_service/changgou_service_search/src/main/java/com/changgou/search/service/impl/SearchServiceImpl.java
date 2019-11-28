package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.Result;
import com.changgou.feign.SkuFeign;
import com.changgou.search.mapper.SearchMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SearchService;
import com.changgou.utils.Page;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/22 9:16
 * @description ：ES实现业务层
 * @version: 1.0
 */
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchMapper searchMapper;
    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Override
    public void importData() {
        Result all = skuFeign.findAll();
        Object data = all.getData();
        List<SkuInfo> skuInfos = JSON.parseArray(JSON.toJSONString(data), SkuInfo.class);
        for (SkuInfo skuInfo : skuInfos) {
            skuInfo.setSpecMap(JSON.parseObject(skuInfo.getSpec(), Map.class));
        }

        searchMapper.saveAll(skuInfos);
    }

    /**
     * 搜索索引库
     *
     * @param searchMap 关键词,过滤条件,分页,查询
     * @return
     */
    @Override
    public Map search(Map<String, String> searchMap) {
        Map result = new HashMap();
        //条件对象
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        //组合条件对象
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (null != searchMap.get("keywords") && !"".equals(searchMap.get("keywords").trim())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("name", searchMap.get("keywords")).operator(Operator.AND));
        } else {
            //默认以前的搜索历史
        }
        String pageNum = searchMap.get("pageNum");
        if (StringUtils.isEmpty(pageNum)) {
            pageNum = "1";
        }


        builder.withPageable(PageRequest.of(Integer.parseInt(pageNum) - 1, Page.pageSize));


        //品牌条件
        if (null != searchMap.get("brand") && !"".equals(searchMap.get("brand").trim())) {
            //filter是过滤的意思 QueryBuilders.termQuery:完全匹配
            boolQueryBuilder.filter(QueryBuilders.termQuery("brandName", searchMap.get("brand")));
        }
        //添加规格条件
       /* Set<String> sets = searchMap.keySet();
        if (null != sets && sets.size() > 0) {
            for (String set : sets) {
                if (set.startsWith("spec_")) {
                    //filter是过滤的意思 QueryBuilders.termQuery:完全匹配
                    boolQueryBuilder.filter(QueryBuilders.termQuery("specMap." + set.substring(5) + ".keyword", searchMap.get(set)));
                }
            }
        }*/
        Set<Map.Entry<String, String>> sets = searchMap.entrySet();
        if (null != sets && sets.size() > 0) {
            for (Map.Entry<String, String> set : sets) {
                if (set.getKey().startsWith("spec_")) {
                    boolQueryBuilder.filter(QueryBuilders.termQuery("specMap." + set.getKey().substring(5) + ".keyword", set.getValue().replace("%2B", "+")));
                }
            }
        }
        //根据价格区间
        if (null != searchMap.get("price") && searchMap.get("price").length() > 0) {
            String[] prices = searchMap.get("price").split("-");
            if (prices.length == 2) {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(prices[0]).lt(prices[1]));
            } else if (prices.length == 1) {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(prices[0]));
            }
        }
        //价格排序
        if (null != searchMap.get("sortRule") && !"".equals(searchMap.get("sortRule"))) {
            if ("ASC".equals(searchMap.get("sortRule"))) {
                builder.withSort(SortBuilders.fieldSort(searchMap.get("sortField")).order(SortOrder.ASC));
            } else {
                builder.withSort(SortBuilders.fieldSort(searchMap.get("sortField")).order(SortOrder.DESC));
            }
        }

        builder.withQuery(boolQueryBuilder);
        //高亮
        HighlightBuilder.Field field = new HighlightBuilder.Field("name");
        field.preTags("<span style='color:red'>");
        field.postTags("</span>");
        builder.withHighlightFields(field);

        //查询规格集合,聚合查询
        String skuSpecName = "skuSpecName";
        TermsAggregationBuilder specName = AggregationBuilders.terms(skuSpecName).field("spec.keyword");
        builder.addAggregation(specName);
        //查询品牌集合 ,聚合查询
        String skuBandName = "skuBandName";
        TermsAggregationBuilder brandName = AggregationBuilders.terms(skuBandName).field("brandName");
        builder.addAggregation(brandName);

        //执行查询
        AggregatedPage<SkuInfo> skuInfos = esTemplate.queryForPage(builder.build(), SkuInfo.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                ArrayList<T> list = new ArrayList<>();//创建集合

                SearchHits hits = searchResponse.getHits();//命中
                SearchHit[] hits1 = hits.getHits();//获取结果集
                //结果集遍历得到对象
                for (SearchHit documentFields : hits1) {
                    String sourceAsString = documentFields.getSourceAsString();//获取所需对象的json格式字符串
                    SkuInfo skuInfo = JSON.parseObject(sourceAsString, SkuInfo.class);//将字符串转换成所需对象

                    //判断是否有高亮
                    Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
                    if (null != highlightFields && highlightFields.size() > 0) {
                        HighlightField name = highlightFields.get("name");
                        Text[] fragments = name.fragments();
                        if (null != fragments && fragments.length > 0) {
                            String s = fragments[0].toString();
                            skuInfo.setName(s);
                        }
                    }
                    list.add((T) skuInfo);
                }


                long totalHits = hits.totalHits;//命中的总条数
                Aggregations aggregations = searchResponse.getAggregations();

                return new AggregatedPageImpl<T>(list, pageable, totalHits, aggregations);
            }
        });

        //获取查询到的品牌集合
        Terms brandTerms = (Terms) skuInfos.getAggregation(skuBandName);
        List<String> brandList = brandTerms.getBuckets().stream().map(b -> b.getKeyAsString()).collect(toList());

        //获取规格集合
        Terms specTerms = (Terms) skuInfos.getAggregation(skuSpecName);
        Map<String, Set<String>> specList = buildModel(specTerms.getBuckets().stream().map(b -> b.getKeyAsString()).collect(toList()));

        //得到商品结果集
        List<SkuInfo> content = skuInfos.getContent();
        result.put("rows", content);
        result.put("pageNum", pageNum);
        result.put("total", skuInfos.getTotalElements());
        result.put("brandList", brandList);
        result.put("specList", specList);
        return result;


    }

    public Map<String, Set<String>> buildModel(List<String> specList) {
        Map<String, Set<String>> map = null;
        if (null != specList && specList.size() > 0) {
            map = new HashMap<>();
            for (String spec : specList) {
                Map<String, String> jsonMap = JSON.parseObject(spec, Map.class);
                Set<Map.Entry<String, String>> entries = jsonMap.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    Set<String> set = map.get(entry.getKey());
                    if (set == null) {
                        set = new HashSet<>();
                    }
                    set.add(entry.getValue());
                    map.put(entry.getKey(), set);
                }
            }
        }
        return map;
    }
}
