package com.changgou.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.IdWorker;
import com.changgou.goods.dao.*;
import com.changgou.goods.service.SpuService;
import com.changgou.pojo.Brand;
import com.changgou.pojo.Category;
import com.changgou.pojo.Sku;
import com.changgou.pojo.Spu;
import com.changgou.vo.CategoryBrand;
import com.changgou.vo.Goods;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询全部列表
     *
     * @return
     */
    @Override
    public List<Spu> findAll() {
        return spuMapper.selectAll();
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    @Override
    public Spu findById(String id) {
        return spuMapper.selectByPrimaryKey(id);
    }


    /**
     * 增加
     *
     * @param spu
     */
    @Override
    public void add(Spu spu) {
        spuMapper.insert(spu);
    }


    /**
     * 修改
     *
     * @param spu
     */
    @Override
    public void update(Spu spu) {
        spuMapper.updateByPrimaryKey(spu);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        spuMapper.deleteByPrimaryKey(id);
    }


    /**
     * 条件查询
     *
     * @param searchMap
     * @return
     */
    @Override
    public List<Spu> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return spuMapper.selectByExample(example);
    }

    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Spu> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        return (Page<Spu>) spuMapper.selectAll();
    }

    /**
     * 条件+分页查询
     *
     * @param searchMap 查询条件
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @Override
    public Page<Spu> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = createExample(searchMap);
        return (Page<Spu>) spuMapper.selectByExample(example);
    }

    /**
     * 添加商品及库存表
     *
     * @param goods
     */
    @Override
    public void addGoods(Goods goods) {
        long l = idWorker.nextId();
        Spu spu = goods.getSpu();
        spu.setId(String.valueOf(l));
        spu.setIsMarketable(Spu.NOTISMARKETABLE);
        spu.setStatus(Spu.NSTATUS);
        //添加商品表
        spuMapper.insertSelective(spu);
        //添加库存表
        addsku(goods);
    }

    private void addsku(Goods goods) {
        //获取spu对象
        Spu spu = goods.getSpu();
        //当前日期
        Date date = new Date();
        //获取品牌对象
        Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
        //获取分类对象
        Category category = categoryMapper.selectByPrimaryKey(spu.getCategory3Id());

        /**
         * 添加分类与品牌之间的关联
         */
        CategoryBrand categoryBrand = new CategoryBrand();
        categoryBrand.setBrandId(spu.getBrandId());
        categoryBrand.setCategoryId(spu.getCategory3Id());
        int count = categoryBrandMapper.selectCount(categoryBrand);
        //判断是否有这个品牌和分类的关系数据
        if (count == 0) {
            //如果没有关系数据则添加品牌和分类关系数据
            categoryBrandMapper.insert(categoryBrand);
        }
        List<Sku> skuList = goods.getSkuList();
        Spu goodsSpu = goods.getSpu();
        for (Sku sku : skuList) {
            sku.setId(String.valueOf(idWorker.nextId()));
            String name = goodsSpu.getName();
            String spec = sku.getSpec();
            Map<String, String> map = JSON.parseObject(spec, Map.class);
            Set<Map.Entry<String, String>> set = map.entrySet();
            for (Map.Entry<String, String> entry : set) {
                name += " " + entry.getValue();
            }
            sku.setName(name);
            sku.setSpuId(goodsSpu.getId());
            skuMapper.insertSelective(sku);
        }
    }

    /**
     * 上架商品
     *
     * @param id
     */
    @Override
    public void updateMaketable(Integer id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (Spu.YSEISDELETE.equals(spu.getIsDelete())) {
            throw new RuntimeException("已删除商品不能上架");
        }
        if (!Spu.YSTATUS.equals(spu.getStatus())) {
            throw new RuntimeException("未审核通过商品不能上架");
        }
        if (Spu.YESISMARKETABLE.equals(spu.getIsMarketable())) {
            throw new RuntimeException("商品已上架,请勿重复上架");
        }
        spu.setIsMarketable("1");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    @Override
    public void updateSpuAndSku(Goods goods) {
        //修改spu
        Spu spu = goods.getSpu();
        spuMapper.updateByPrimaryKey(spu);
        //删除sku
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId", spu.getId());
        skuMapper.deleteByExample(example);
        //添加sku
        addsku(goods);

    }

    /**
     * 修改商品状态下架
     *
     * @param id
     */
    @Override
    public void pull(String id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        spu.setIsMarketable(Spu.NOTISMARKETABLE);
        spuMapper.updateByPrimaryKey(spu);
    }

    /**
     * 构建查询对象
     *
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (searchMap != null) {
            // 主键
            if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                criteria.andLike("id", "%" + searchMap.get("id") + "%");
            }
            // 货号
            if (searchMap.get("sn") != null && !"".equals(searchMap.get("sn"))) {
                criteria.andLike("sn", "%" + searchMap.get("sn") + "%");
            }
            // SPU名
            if (searchMap.get("name") != null && !"".equals(searchMap.get("name"))) {
                criteria.andLike("name", "%" + searchMap.get("name") + "%");
            }
            // 副标题
            if (searchMap.get("caption") != null && !"".equals(searchMap.get("caption"))) {
                criteria.andLike("caption", "%" + searchMap.get("caption") + "%");
            }
            // 图片
            if (searchMap.get("image") != null && !"".equals(searchMap.get("image"))) {
                criteria.andLike("image", "%" + searchMap.get("image") + "%");
            }
            // 图片列表
            if (searchMap.get("images") != null && !"".equals(searchMap.get("images"))) {
                criteria.andLike("images", "%" + searchMap.get("images") + "%");
            }
            // 售后服务
            if (searchMap.get("sale_service") != null && !"".equals(searchMap.get("sale_service"))) {
                criteria.andLike("sale_service", "%" + searchMap.get("sale_service") + "%");
            }
            // 介绍
            if (searchMap.get("introduction") != null && !"".equals(searchMap.get("introduction"))) {
                criteria.andLike("introduction", "%" + searchMap.get("introduction") + "%");
            }
            // 规格列表
            if (searchMap.get("spec_items") != null && !"".equals(searchMap.get("spec_items"))) {
                criteria.andLike("spec_items", "%" + searchMap.get("spec_items") + "%");
            }
            // 参数列表
            if (searchMap.get("para_items") != null && !"".equals(searchMap.get("para_items"))) {
                criteria.andLike("para_items", "%" + searchMap.get("para_items") + "%");
            }
            // 是否上架
            if (searchMap.get("is_marketable") != null && !"".equals(searchMap.get("is_marketable"))) {
                criteria.andLike("is_marketable", "%" + searchMap.get("is_marketable") + "%");
            }
            // 是否启用规格
            if (searchMap.get("is_enable_spec") != null && !"".equals(searchMap.get("is_enable_spec"))) {
                criteria.andLike("is_enable_spec", "%" + searchMap.get("is_enable_spec") + "%");
            }
            // 是否删除
            if (searchMap.get("is_delete") != null && !"".equals(searchMap.get("is_delete"))) {
                criteria.andLike("is_delete", "%" + searchMap.get("is_delete") + "%");
            }
            // 审核状态
            if (searchMap.get("status") != null && !"".equals(searchMap.get("status"))) {
                criteria.andLike("status", "%" + searchMap.get("status") + "%");
            }

            // 品牌ID
            if (searchMap.get("brandId") != null) {
                criteria.andEqualTo("brandId", searchMap.get("brandId"));
            }
            // 一级分类
            if (searchMap.get("category1Id") != null) {
                criteria.andEqualTo("category1Id", searchMap.get("category1Id"));
            }
            // 二级分类
            if (searchMap.get("category2Id") != null) {
                criteria.andEqualTo("category2Id", searchMap.get("category2Id"));
            }
            // 三级分类
            if (searchMap.get("category3Id") != null) {
                criteria.andEqualTo("category3Id", searchMap.get("category3Id"));
            }
            // 模板ID
            if (searchMap.get("templateId") != null) {
                criteria.andEqualTo("templateId", searchMap.get("templateId"));
            }
            // 运费模板id
            if (searchMap.get("freightId") != null) {
                criteria.andEqualTo("freightId", searchMap.get("freightId"));
            }
            // 销量
            if (searchMap.get("saleNum") != null) {
                criteria.andEqualTo("saleNum", searchMap.get("saleNum"));
            }
            // 评论数
            if (searchMap.get("commentNum") != null) {
                criteria.andEqualTo("commentNum", searchMap.get("commentNum"));
            }

        }
        return example;
    }

}
