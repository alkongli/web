package com.example.dao;

import com.example.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表
 * 
 * @author z
 * @email 2257957360@qq.com
 * @date 2022-12-16 18:58:13
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {
	
}
