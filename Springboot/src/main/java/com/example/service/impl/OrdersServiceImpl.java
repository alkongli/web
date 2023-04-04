package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.common.CustomException;
import com.example.common.Result;
import com.example.dao.AddressDao;
import com.example.dto.OrdersDto;
import com.example.entity.AddressEntity;
import com.example.entity.OrderProductEntity;
import com.example.entity.ProductEntity;
import com.example.service.AddressService;
import com.example.service.OrderProductService;
import com.example.service.ProductService;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.example.dao.OrdersDao;
import com.example.entity.OrdersEntity;
import com.example.service.OrdersService;

import javax.annotation.Resource;


@Service("ordersService")
public class OrdersServiceImpl extends ServiceImpl<OrdersDao, OrdersEntity> implements OrdersService {

    @Resource
    private ProductService productService;

    @Resource
    private OrderProductService orderProductService;

    @Resource
    private AddressDao addressDao;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 用户查询订单列表
     */
    public List<OrdersDto> getUserOrders(Long userId,Integer status){
        LambdaQueryWrapper<OrdersEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId!=null,OrdersEntity::getUserId,userId);
        queryWrapper.eq(status!=null&&status>0,OrdersEntity::getStatus,status);
        queryWrapper.orderByDesc(OrdersEntity::getOrderTime);
        List<OrdersEntity> list =this.list(queryWrapper);

        List<OrdersDto> list2=list.stream().map((item)->{
            OrdersDto ordersDto=new OrdersDto();

            AddressEntity addressServiceById = addressDao.getAllAddressById(item.getAddressId());

            LambdaQueryWrapper<OrderProductEntity> queryWrapper1=new LambdaQueryWrapper<>();
            queryWrapper1.eq(OrderProductEntity::getOrderId,item.getId());
            List<OrderProductEntity> list1 = orderProductService.list(queryWrapper1);

            BeanUtils.copyProperties(item,ordersDto);

            ordersDto.setAddress(addressServiceById.getAddress());
            ordersDto.setConsignee(addressServiceById.getConsignee());
            ordersDto.setPhone(addressServiceById.getPhone());
            ordersDto.setOrderProduct(list1);

            return ordersDto;
        }
        ).collect(Collectors.toList());

        return list2;
    }

    /**
     * 生成秒杀订单
     */
    public void addKillOrders(OrdersDto orders){
        orders.setOrderTime(LocalDateTime.now());
        List<OrderProductEntity> orderProduct = orders.getOrderProduct();

        for (OrderProductEntity orderProductEntity : orderProduct) {

            //        商品库存减一
            LambdaUpdateWrapper<ProductEntity> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(ProductEntity::getId,orderProductEntity.getProductId());

            updateWrapper.setSql("stock=stock-1");
            productService.update(updateWrapper);

            orderProductEntity.setOrderId(orders.getId());
            orderProductService.save(orderProductEntity);
        }
        this.save(orders);
    }

    /**
     * 生成订单
     */
    public Result<String> addOrders(OrdersDto orders){
        orders.setOrderTime(LocalDateTime.now());
        List<OrderProductEntity> orderProduct = orders.getOrderProduct();

        for (OrderProductEntity orderProductEntity : orderProduct) {
            LambdaQueryWrapper<ProductEntity> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(ProductEntity::getId,orderProductEntity.getProductId());
            ProductEntity productServiceById = productService.getOne(queryWrapper);

            if(productServiceById.getStock()<1)
                return Result.error("商品: "+productServiceById.getName()+"已售空!");
            else if(productServiceById.getStock()<orderProductEntity.getNumber())
                return Result.error("商品: "+productServiceById.getName()+"库存不足!");
            else if(productServiceById.getStatus()==1)
                return Result.error("商品: "+productServiceById.getName()+"已下架!");

        }

        for (OrderProductEntity orderProductEntity : orderProduct) {

            //        商品库存减购买数量
            LambdaUpdateWrapper<ProductEntity> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(ProductEntity::getId,orderProductEntity.getProductId());
            updateWrapper.setSql("stock=stock-"+orderProductEntity.getNumber());
            productService.update(updateWrapper);

            orderProductEntity.setOrderId(orders.getId());
            orderProductService.save(orderProductEntity);
        }
        this.save(orders);
        return Result.success("下单成功");
    }


    /**
     * 取消订单，修改商品库存
     */
    public void returnProduct(OrdersDto orders){
        List<OrderProductEntity> orderProduct = orders.getOrderProduct();
        for (OrderProductEntity orderProductEntity : orderProduct) {

            //        商品库存增加购买数量
            LambdaUpdateWrapper<ProductEntity> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(ProductEntity::getId,orderProductEntity.getProductId());
            updateWrapper.setSql("stock=stock+"+orderProductEntity.getNumber());
            productService.update(updateWrapper);

            //redis中库存增加
            String qtKey="sk:"+orderProductEntity.getProductId()+":qt";
            if(stringRedisTemplate.hasKey(qtKey)){
                stringRedisTemplate.opsForValue().increment(qtKey,orderProductEntity.getNumber());
            }

        }
    }

}