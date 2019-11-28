package com.changgou.controller;

import com.changgou.search.feign.SearchFeign;
import com.changgou.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/22 20:20
 * @description ：跳转和渲染页面
 * @version: 1.0
 */
@Controller//只需跳转页面,所以不可以用@RestController
@RequestMapping("/wsearch")
public class WebSearchController {
    @Autowired
    private SearchFeign searchFeign;

    public Map<String, String> handlerModel(Map<String, String> searchMap) {
        if (searchMap != null && searchMap.size() > 0) {
            Set<Map.Entry<String, String>> set = searchMap.entrySet();
            for (Map.Entry<String, String> entry : set) {
                if (entry.getKey().startsWith("spec_")) {
                    searchMap.put(entry.getKey(), entry.getValue().replace("+", "%2B"));
                }
            }
        }
        return searchMap;
    }

    @GetMapping//使用Get提交是方便从登陆页面继续跳转回来搜索页面,如果是post表单提交则无法跳转回来
    public String search(Model model, @RequestParam Map<String, String> searchMap) {
        handlerModel(searchMap);
        Map search = searchFeign.search(searchMap);
        model.addAttribute("searchMap", searchMap);
        model.addAttribute("result", search);
        model.addAttribute("page", new Page(
                (int) search.get("total"),
                Integer.parseInt(String.valueOf(search.get("pageNum"))),
                Page.pageSize
        ));
        StringBuilder url = new StringBuilder();
        url.append("http://search.changgou.com:9011/wsearch");
        if (null != searchMap && searchMap.size() > 0) {
            url.append("?");

            Set<Map.Entry<String, String>> entries = searchMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                if ("pageNum".equals(entry.getKey())||"sortRule".equals(entry.getKey())||"sortField".equals(entry.getKey())) {
                }else{
                    url.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
               // sortRule=ASC&sortField=price
                if("sortRule".equals(entry.getKey())){
                    model.addAttribute("sortRule",entry.getValue());
                }
                if("sortField".equals(entry.getKey())){
                    model.addAttribute("sortField",entry.getValue());
                }
            }
        }
        model.addAttribute("url", url.toString());
        return "search";
    }
}
