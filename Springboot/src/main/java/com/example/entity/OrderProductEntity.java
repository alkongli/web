package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 订单商品列表
 * 
 * @author z
 * @email 2257957360@qq.com
 * @date 2023-03-14 21:40:19
 */
@Data
@TableName("order_product")
public class OrderProductEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 订单号
	 */
	private Long orderId;
	/**
	 * 商品id
	 */
	private Long productId;
	/**
	 * 商品名称
	 */
	private String name;
	/**
	 * 商品图片
	 */
	private String img;

	/**
	 * 商品单价
	 */
	private Integer price;
	/**
	 * 商品颜色
	 */
	private String color;
	/**
	 * 商品尺寸
	 */
	private String size;
	/**
	 * 商品数量
	 */
	private Integer number;

}
