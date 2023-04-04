package com.example.dao;

import com.example.entity.AddressEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 地址管理
 * 
 * @author z
 * @email 2257957360@qq.com
 * @date 2022-12-16 18:58:13
 */
@Mapper
public interface AddressDao extends BaseMapper<AddressEntity> {
    /**
     * 可以查询被逻辑删除的地址
     */
    @Select("select * from address where id=#{id}")
    public AddressEntity getAllAddressById(@Param("id") Long id);
	
}
