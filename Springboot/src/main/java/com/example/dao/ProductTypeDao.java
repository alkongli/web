package com.example.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.ProductTypeEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductTypeDao extends BaseMapper<ProductTypeEntity> {

}