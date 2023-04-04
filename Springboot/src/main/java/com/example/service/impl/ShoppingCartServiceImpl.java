package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.CustomException;
import com.example.dto.ShoppingCartDto;
import com.example.entity.ProductEntity;
import com.example.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.dao.ShoppingCartDao;
import com.example.entity.ShoppingCartEntity;
import com.example.service.ShoppingCartService;

import javax.annotation.Resource;


@Service("shoppingCartService")
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartDao, ShoppingCartEntity> implements ShoppingCartService {

    @Resource
    private ProductService productService;

    public List<ShoppingCartDto> getList(Long userId){

        LambdaQueryWrapper<ShoppingCartEntity> queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.eq(ShoppingCartEntity::getUserId,userId);
//        添加排序
        queryWrapper.orderByAsc(ShoppingCartEntity::getCreateTime);
//        执行查询
        List<ShoppingCartEntity> list = this.list(queryWrapper);

        List<ShoppingCartDto> list1=list.stream().map((item)->{
            ShoppingCartDto shoppingCartDto=new ShoppingCartDto();

            BeanUtils.copyProperties(item,shoppingCartDto);

            ProductEntity productServiceById = productService.getById(item.getProductId());

            shoppingCartDto.setProductEntity(productServiceById);

            return shoppingCartDto;
        }
        ).collect(Collectors.toList());

        return list1;
    }
}