package com.example.dao;

import com.example.dto.ShoppingCartDto;
import com.example.entity.ShoppingCartEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 购物车
 * 
 * @author z
 * @email 2257957360@qq.com
 * @date 2023-03-17 15:30:46
 */
@Mapper
public interface ShoppingCartDao extends BaseMapper<ShoppingCartEntity> {

    public List<ShoppingCartDto> getList(Long userId);
}
