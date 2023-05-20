package com.example.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 商品表
 * 
 * @author z
 * @email 2257957360@qq.com
 * @date 2022-12-16 18:58:13
 */
@Data
@TableName("product")
public class ProductEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 商品id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 商品名
	 */
	private String name;
	/**
	 * 商品描述，300字以内
	 */
	private String text;
	/**
	 * 商品类型id
	 */
	private Long typeId;
	/**
	 * 商品状态 true正常售卖 ，false 已下架
	 */
	private Integer status;
	/**
	 * 商品库存
	 */
	private Integer stock;
	/**
	 * 商品展示图片
	 */
	private String img;

	/**
	 * 商品价格
	 */
	private Long price;
	/**
	 * 商品折扣
	 */
	private BigDecimal discount;
	/**
	 * 商品颜色列表
	 */
	private String colorList;
	/**
	 *商品尺寸
	 */
	private String sizeList;
	/**
	 * 商品创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;
	/**
	 * 商品更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

	/**
	 * 商品更新员工
	 */
	private Long employeeId;

}
