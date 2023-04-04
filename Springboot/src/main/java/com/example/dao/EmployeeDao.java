package com.example.dao;

import com.example.entity.EmployeeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 员工信息
 * 
 * @author z
 * @email 2257957360@qq.com
 * @date 2022-12-16 18:58:13
 */
@Mapper
public interface EmployeeDao extends BaseMapper<EmployeeEntity> {
	
}
