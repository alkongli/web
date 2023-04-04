package com.example.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 用户表
 * 
 * @author z
 * @email 2257957360@qq.com
 * @date 2022-12-16 18:58:13
 */
@Data
@TableName("user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 用户昵称
	 */
	private String name;
	/**
	 * 用户手机号
	 */
	private String phone;
	/**
	 * 性别 0代表女，1代表男
	 */
	private Integer sex;
	/**
	 * 账号状态 0代表禁用，1代表正常
	 */
	private Integer status;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 用户验证码（数据库账中不存在）
	 */
	@TableField(exist = false)
	private String code;
}
