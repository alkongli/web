package com.example.controller;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.dto.ProductDto;
import com.example.entity.ProductTypeEntity;
import com.example.service.ProductTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import com.example.entity.ProductEntity;
import com.example.service.ProductService;


import javax.annotation.Resource;


/**
 * 商品表
 *
 * @author z
 * @email 2257957360@qq.com
 * @date 2022-12-16 18:58:13
 */
@Slf4j
@Api(tags="商品管理")
@RestController
@RequestMapping("product")
public class ProductController {
    @Resource
    private ProductService productService;

    @Resource
    private ProductTypeService productTypeService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize,int secKill,String name){
//        构造分页构造器
        Page<ProductEntity> pageInfo=new Page<>(page,pageSize);
        Page ProductPage=new Page();

        LambdaQueryWrapper<ProductEntity> queryWrapper=new LambdaQueryWrapper();

//        添加排序
        queryWrapper.orderByDesc(ProductEntity::getCreateTime);

        if(secKill==1)
            queryWrapper.eq(ProductEntity::getTypeId,1);
        else
            queryWrapper.ge(ProductEntity::getTypeId,2);

        if(name!=null)
            queryWrapper.likeRight(ProductEntity::getName,name);

//        执行查询
        productService.page(pageInfo,queryWrapper);

//        对象拷贝
        BeanUtils.copyProperties(pageInfo,ProductPage,"records");

        List<ProductEntity> records=pageInfo.getRecords();

        List<ProductDto> list=records.stream().map((item)->{
            Long typeId=item.getTypeId();
            ProductDto productDto=new ProductDto();
            BeanUtils.copyProperties(item,productDto);
            ProductTypeEntity byId = productTypeService.getById(typeId);
            productDto.setType(byId.getType());
            return productDto;
        }).collect(Collectors.toList());

        ProductPage.setRecords(list);

        return Result.success(ProductPage);
    }



    /**
     * 列表(搜索)
     */
    @GetMapping("list")
    public Result<List<ProductEntity>> list(String name,Long typeId){
        if(name==null&&typeId==null)
            return Result.error("参数错误");

        LambdaQueryWrapper<ProductEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(name!=null,ProductEntity::getName,name);
        queryWrapper.eq(typeId!=null,ProductEntity::getTypeId,typeId);
        queryWrapper.eq(ProductEntity::getStatus,0);
        queryWrapper.orderByDesc(ProductEntity::getUpdateTime);
        List<ProductEntity> list = productService.list(queryWrapper);

        return Result.success(list);
    }

    /**
     *商品分类
     */
    @ApiOperation(value = "根据商品类型查询")
    @GetMapping("typeId")
    public Result<List<ProductEntity>> type(Long typeId){
        if(typeId==null){
            Result.error("参数错误!");
        }
        String key="product:"+typeId;
        String s = stringRedisTemplate.opsForValue().get(key);
        if (s!=null){
            if(s.equals("暂无数据"))
                return Result.success(null);
            List<ProductEntity> listRedis = JSONUtil.toList(JSONUtil.parseArray(JSONUtil.toJsonStr(s)), ProductEntity.class);
            return Result.success(listRedis);
        }

        LambdaQueryWrapper<ProductEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductEntity::getTypeId,typeId);
        queryWrapper.eq(ProductEntity::getStatus,0);
        queryWrapper.orderByDesc(ProductEntity::getUpdateTime);
        List<ProductEntity> list = productService.list(queryWrapper);

        //判断list是否为空

        if(list.isEmpty()){
            double r=Math.random();
            int t=(int)r*5+1;
            stringRedisTemplate.opsForValue().set(key,"暂无数据",t,TimeUnit.MINUTES);
        }

        else
            stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(list),6,TimeUnit.HOURS);

        return Result.success(list);
    }


    /**
     * 信息
     */

    @GetMapping("/info/{id}")
    public Result<ProductEntity> info(@PathVariable("id") Long id){
		ProductEntity product = productService.getById(id);

        return Result.success(product);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Result<String> save(@RequestBody ProductEntity product){
		productService.save(product);

        String key="product:"+product.getTypeId();
        stringRedisTemplate.delete(key);
        return Result.success("保存成功");
    }

    /**
     * 修改
     */
    @PostMapping("update")
    public Result<String> update(@RequestBody ProductEntity product){
        ProductEntity productServiceById = productService.getById(product);

        productService.updateById(product);
		try {
            //若修改商品类型，则删除该类型redis
            if(product.getTypeId()!=null&&product.getTypeId()>0){
                String key="product:"+product.getTypeId();
                stringRedisTemplate.delete(key);
            }
        }catch (Exception e){
		    log.error(String.valueOf(e));
        }
        stringRedisTemplate.delete("product:"+productServiceById.getTypeId());
		if(product.getStatus()!=null){
            if(product.getStatus()==0)
                return Result.success("上架成功");
            else if(product.getStatus()==1)
                return Result.success("下架成功");
        }
        return Result.success("修改成功");
    }

    /**
     * 删除
     */
    @DeleteMapping
    public Result<String> delete(@RequestBody ProductEntity product){
        log.info(String.valueOf(product));
		productService.removeById(product.getId());
		try {
            //若删除商品类型，则删除该类型redis
            String key="product:"+product.getTypeId();
            stringRedisTemplate.delete(key);
        }catch (Exception e){
		    log.error(String.valueOf(e));
        }

        return Result.success("删除成功");
    }

}
