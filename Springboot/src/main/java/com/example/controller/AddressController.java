package com.example.controller;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.common.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import com.example.entity.AddressEntity;
import com.example.service.AddressService;


import javax.annotation.Resource;


/**
 * 地址管理
 *
 * @author z
 * @email 2257957360@qq.com
 * @date 2022-12-16 18:58:13
 */
@Api(tags="地址管理")
@RestController
@RequestMapping("address")
public class AddressController {
    @Resource
    private AddressService addressService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public Result<List<AddressEntity>> list(Long userId){


        LambdaQueryWrapper<AddressEntity> queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.eq(AddressEntity::getUserId,userId);
//        添加排序
        queryWrapper.orderByAsc(AddressEntity::getCreateTime);
//        执行查询
        List<AddressEntity> list = addressService.list(queryWrapper);

        return Result.success(list);
    }


    /**
     * 信息
     */
    @ApiOperation(value = "id查询")
    @GetMapping("/info/{id}")
    public Result<AddressEntity> info(@PathVariable("id") Long id){

        AddressEntity addressEntity = addressService.getById(id);

        return Result.success(addressEntity);
    }

    /**
     * 保存
     */

    @PostMapping("/save")
    public Result<String> save(@RequestBody AddressEntity address){
        addressService.save(address);

        return Result.success("保存成功");
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改")
    @PutMapping
    public Result<String> update(@RequestBody AddressEntity address){
        addressService.updateById(address);
        return Result.success("修改成功");
    }

    /**
     * 修改默认地址
     */
    @ApiOperation(value = "修改默认地址")
    @PutMapping("/default")
    public Result<String> defaultAddress(@RequestBody AddressEntity address){
        LambdaUpdateWrapper<AddressEntity> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressEntity::getUserId,address.getUserId());
        updateWrapper.set(AddressEntity::getIsDefault,0);
        addressService.update(updateWrapper);
        addressService.updateById(address);
        return Result.success("默认地址修改成功");
    }

    /**
     * 删除
     */
    @DeleteMapping
    public Result<String> delete(Long id){
        addressService.removeById(id);
        return Result.success("删除成功");
    }

}
