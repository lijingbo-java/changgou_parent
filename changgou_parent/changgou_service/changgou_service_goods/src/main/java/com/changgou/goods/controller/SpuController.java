package com.changgou.goods.controller;

import com.changgou.entity.PageResult;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.goods.service.SpuService;
import com.changgou.pojo.Spu;
import com.changgou.vo.Goods;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/spu")
public class SpuController {


    @Autowired
    private SpuService spuService;

    /**
     * 修改商品下架状态
     *
     * @param id
     * @return
     */
    @PutMapping("/pull/{id}")//redtful风格
    public Result pull(@PathVariable String id) {
        spuService.pull(id);
        return new Result(true, StatusCode.OK, "下架成功");
    }

    /**
     * 根据SpuId查询Spu对象
     */
    @GetMapping("/findSpuById")
    public Spu findSpuById(String spuId) {
        return spuService.findById(spuId);
    }

    /**
     * 修改商品上架
     *
     * @param id 商品id
     */
    @PutMapping("/updateMaketable")//传统带参风格
    public Result updateMaketable(@RequestParam Integer id) {
        spuService.updateMaketable(id);
        return new Result(true, StatusCode.OK, "上架成功");
    }

    /**
     * 添加商品,同时添加多个库存
     *
     * @param goods
     * @return
     */
    @PostMapping("/add")
    public Result addGoods(@RequestBody Goods goods) {
        spuService.addGoods(goods);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    /**
     * 查询全部数据
     *
     * @return
     */
    @GetMapping
    public Result findAll() {
        List<Spu> spuList = spuService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", spuList);
    }

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable String id) {
        Spu spu = spuService.findById(id);
        return new Result(true, StatusCode.OK, "查询成功", spu);
    }


    /***
     * 新增数据
     * @param spu
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Spu spu) {
        spuService.add(spu);
        return new Result(true, StatusCode.OK, "添加成功");
    }


    /***
     * 修改数据
     * @param goods
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody Goods goods, @PathVariable String id) {
        goods.getSpu().setId(id);
        spuService.updateSpuAndSku(goods);
        return new Result(true, StatusCode.OK, "修改成功");
    }


    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable String id) {
        spuService.delete(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/search")
    public Result findList(@RequestParam Map searchMap) {
        List<Spu> list = spuService.findList(searchMap);
        return new Result(true, StatusCode.OK, "查询成功", list);
    }


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}")
    public Result findPage(@RequestParam Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<Spu> pageList = spuService.findPage(searchMap, page, size);
        PageResult pageResult = new PageResult(pageList.getTotal(), pageList.getResult());
        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }


}
