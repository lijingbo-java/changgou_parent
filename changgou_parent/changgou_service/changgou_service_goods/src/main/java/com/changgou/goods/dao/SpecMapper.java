package com.changgou.goods.dao;

import com.changgou.pojo.Spec;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpecMapper extends Mapper<Spec> {
    @Select("SELECT ts.* FROM tb_category tc JOIN tb_spec ts on tc.template_id=ts.template_id WHERE tc.`name`=#{wocao} and tc.parent_id !=0")
    List<Spec> findSpecByCategoryName(@Param(value = "wocao") String name);
}
