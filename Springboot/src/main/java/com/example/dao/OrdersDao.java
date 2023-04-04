package com.example.dao;

import com.example.common.Result;
import com.example.dto.OrdersDto;
import com.example.entity.AddressEntity;
import com.example.entity.OrdersEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 订单表
 * 
 * @author z
 * @email 2257957360@qq.com
 * @date 2022-12-16 18:58:13
 */
@Mapper
public interface OrdersDao extends BaseMapper<OrdersEntity> {

    /**
     * 用户查询订单列表
     */
    public List<OrdersDto> getUserOrders(Long userId,Integer status);


    /**
     * 生成秒杀订单
     */
    public void addKillOrders(OrdersDto orders);


    /**
     * 生成订单
     */
    public Result<String> addOrders(OrdersDto orders);


    /**
     * 取消订单，修改商品库存
     */
    public void returnProduct(OrdersDto orders);
}
