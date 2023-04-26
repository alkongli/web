package com.example.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.AddressEntity;
import com.example.entity.ProductTypeEntity;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductTypeDao extends BaseMapper<ProductTypeEntity> {
    /**
     * 可以查询所有type
     */
    @Select("select * from product_type")
    public List<ProductTypeEntity> getAllType();
}