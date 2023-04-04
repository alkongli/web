package com.example.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 地址管理
 * 
 * @author z
 * @email 2257957360@qq.com
 * @date 2022-12-16 18:58:13
 */
@Data
@TableName("address")
public class AddressEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 收货人
	 */
	private String consignee;
	/**
	 * 性别 0 女 1 男
	 */
	private Integer sex;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 地址信息
	 */
	private String address;
	/**
	 * 默认地址 0 否 1是
	 */
	private Integer isDefault;
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

	/**
	 * 逻辑删除 0未删除，1已删除
	 */
	@TableLogic
	private int isDel;

}
