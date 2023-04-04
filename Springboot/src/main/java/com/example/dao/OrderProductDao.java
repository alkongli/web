package com.example.dao;

import com.example.entity.OrderProductEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单商品列表
 * 
 * @author z
 * @email 2257957360@qq.com
 * @date 2023-03-14 21:40:19
 */
@Mapper
public interface OrderProductDao extends BaseMapper<OrderProductEntity> {
	
}
