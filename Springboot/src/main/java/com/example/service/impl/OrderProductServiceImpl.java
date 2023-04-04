package com.example.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.dao.OrderProductDao;
import com.example.entity.OrderProductEntity;
import com.example.service.OrderProductService;


@Service("orderProductService")
public class OrderProductServiceImpl extends ServiceImpl<OrderProductDao, OrderProductEntity> implements OrderProductService {

}