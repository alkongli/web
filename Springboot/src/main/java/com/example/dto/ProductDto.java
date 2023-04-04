package com.example.dto;

import com.example.entity.ProductEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductDto extends ProductEntity {
    /**
     * 秒杀开始时间
     */
    private Integer time;

    /**
     * 商品类型
     */
    private String type;
}
