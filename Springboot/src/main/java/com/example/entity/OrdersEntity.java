package com.example.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.math.BigDecimal;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 订单表
 * 
 * @author z
 * @email 2257957360@qq.com
 * @date 2022-12-16 18:58:13
 */
@Data
@TableName("orders")
public class OrdersEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键 订单号
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 订单状态 1待付款，2待派送，3待签收，4已完成，5已取消
	 */
	private Integer status;
	/**
	 * 下单用户
	 */
	private Long userId;
	/**
	 * 地址id
	 */
	private Long addressId;
	/**
	 * 秒杀商品id
	 */
	private Long productId;
	/**
	 * 下单时间
	 */
	private LocalDateTime orderTime;
	/**
	 * 结账时间
	 */
	private LocalDateTime checkoutTime;
	/**
	 * 实收金额
	 */
	private BigDecimal amount;
	/**
	 * 备注
	 */
	private String remark;

}
