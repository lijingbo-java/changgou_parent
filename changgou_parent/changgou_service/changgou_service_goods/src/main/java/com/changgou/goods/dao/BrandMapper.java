package com.changgou.goods.dao;

import com.changgou.pojo.Brand;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PathVariable;
import tk.mybatis.mapper.common.Mapper;

import javax.websocket.server.PathParam;
import java.util.List;

public interface BrandMapper extends Mapper<Brand> {
    //根据分类筛选品牌
    @Select("select tb.* from tb_brand tb " +
            "JOIN tb_category_brand tcb on tb.id=tcb.brand_id " +
            "JOIN tb_category tc on tc.id=tcb.category_id " +
            "where tc.`name`= #{name}")
    List<Brand> findBrandByCategoryName(String name);

}
