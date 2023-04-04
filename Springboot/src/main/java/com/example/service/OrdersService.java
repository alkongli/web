package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.Result;
import com.example.dto.OrdersDto;
import com.example.entity.AddressEntity;
import com.example.entity.OrdersEntity;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 订单表
 *
 * @author z
 * @email 2257957360@qq.com
 * @date 2022-12-16 18:58:13
 */
public interface OrdersService extends IService<OrdersEntity> {


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

