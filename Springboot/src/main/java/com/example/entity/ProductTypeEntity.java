package com.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
/**
 * 商品类型表
 *
 * @author z
 * @email 2257957360@qq.com
 * @date 2022-12-16 18:58:13
 */
@Data
@TableName("product_type")
public class ProductTypeEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 商品类型id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 类型
     */
    private String type;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
