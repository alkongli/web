package com.example.dto;


import com.example.entity.OrdersEntity;
import com.example.entity.ProductEntity;
import com.example.entity.ShoppingCartEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class ShoppingCartDto extends ShoppingCartEntity {

    /**
     * 商品信息
     */
    private ProductEntity productEntity;



}
