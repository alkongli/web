package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dao.ProductTypeDao;
import com.example.entity.ProductTypeEntity;
import com.example.service.ProductTypeService;
import org.springframework.stereotype.Service;

/**
 * 商品类型表
 *
 * @author z
 * @email 2257957360@qq.com
 * @date 2022-12-16 18:58:13
 */
@Service
public class ProductTypeServiceImpl extends ServiceImpl<ProductTypeDao, ProductTypeEntity> implements ProductTypeService {
}
