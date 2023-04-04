package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.ProductTypeEntity;
import com.example.service.ProductTypeService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Api(tags="商品分类")
@RequestMapping("productType")
@RestController
public class ProductTypeController {
    @Resource
    private ProductTypeService productTypeService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize,String type){
//        构造分页构造器
        Page<ProductTypeEntity> pageInfo=new Page<>(page,pageSize);

        LambdaQueryWrapper<ProductTypeEntity> queryWrapper=new LambdaQueryWrapper();

        if(type!=null)
            queryWrapper.likeRight(ProductTypeEntity::getType,type);

//        添加排序
        queryWrapper.orderByAsc(ProductTypeEntity::getCreateTime);
//        执行查询
        productTypeService.page(pageInfo,queryWrapper);

        return Result.success(pageInfo);
    }

    @GetMapping("list")
    public Result<List<ProductTypeEntity>> getList(){
        List<ProductTypeEntity> list = productTypeService.list();
        return Result.success(list);
    }

    @PostMapping
    public Result<String> add(@RequestBody ProductTypeEntity productType){
        productTypeService.save(productType);
        return Result.success("添加成功");
    }

    @PutMapping
    public Result<String> update(@RequestBody ProductTypeEntity productType){
        productTypeService.updateById(productType);
        return Result.success("修改成功");
    }

    @DeleteMapping
    public Result<String> delete(@RequestBody ProductTypeEntity productType){
        productTypeService.removeById(productType);
        return Result.success("删除成功");
    }
}
