package com.example.dto;

import com.example.entity.OrderProductEntity;
import com.example.entity.OrdersEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrdersDto extends OrdersEntity {

    /**
     * 订单商品信息
     */
    private List<OrderProductEntity> orderProduct;

    /**
     * 收货人
     */
    private String consignee;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 地址信息
     */
    private String address;

}
