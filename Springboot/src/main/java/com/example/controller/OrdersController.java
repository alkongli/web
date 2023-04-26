package com.example.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;

import com.example.dto.OrdersDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import com.example.entity.OrdersEntity;
import com.example.service.OrdersService;

import javax.annotation.Resource;


/**
 * 订单表
 *
 * @author z
 * @email 2257957360@qq.com
 * @date 2022-12-16 18:58:13
 */
@Api(tags="订单管理")
@RestController
@RequestMapping("orders")
public class OrdersController {
    @Resource
    private OrdersService ordersService;

    /**
     * 分页查询
     */
    @ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize,Long id){
//        构造分页构造器
        Page<OrdersEntity> pageInfo=new Page<>(page,pageSize);

        LambdaQueryWrapper<OrdersEntity> queryWrapper=new LambdaQueryWrapper();

        if(id!=null)
            queryWrapper.likeRight(OrdersEntity::getId,id);

//        添加排序
        queryWrapper.orderByDesc(OrdersEntity::getOrderTime);
//        执行查询
        ordersService.page(pageInfo,queryWrapper);

        return Result.success(pageInfo);
    }

    /**
     * 列表
     */
    @ApiOperation(value = "根据条件查询（status，userId）")
    @GetMapping("list")
    public Result<List<OrdersEntity>> list(Integer status,Long userId){
        if(status==null&&userId==null)
            return Result.error("参数错误");

        LambdaQueryWrapper<OrdersEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(status!=null,OrdersEntity::getStatus,status);
        queryWrapper.eq(userId!=null,OrdersEntity::getUserId,userId);
        queryWrapper.orderByDesc(OrdersEntity::getOrderTime);
        List<OrdersEntity> list = ordersService.list(queryWrapper);

        return Result.success(list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public Result<OrdersEntity> info(@PathVariable("id") Long id){
		OrdersEntity orders = ordersService.getById(id);

        return Result.success(orders);
    }


    /**
     * 用户获取订单信息
     */
    @GetMapping("/user/info")
    public Result<List<OrdersDto>> OrdersInfo(Long userId,Integer status){
        List<OrdersDto> list = ordersService.getUserOrders(userId,status);
        return Result.success(list);
    }

    /**
     * 获取平台监控数据
     */
    @PostMapping("/getOrderMonitor")
    public Map<String,Object> getOrderMonitor(@RequestBody Map<String,String[]> day){
        return ordersService.getOrderMonitor(day.get("day1"),day.get("day2"));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Result<String> save(@RequestBody OrdersDto orders){
        orders.setId(Result.getId());
        return ordersService.addOrders(orders);
    }

    /**
     * 修改
     */
    @PutMapping
    public Result<String> update(@RequestBody OrdersDto orders){
        if(orders.getStatus()==2)
            orders.setCheckoutTime(LocalDateTime.now());
        if(orders.getStatus()==5)
            ordersService.returnProduct(orders);
		ordersService.updateById(orders);
        return Result.success("修改成功");
    }

    /**
     * 删除
     */
    @DeleteMapping
    public Result<String>  delete(@RequestBody Long[] ids){
		ordersService.removeByIds(Arrays.asList(ids));

        return Result.success("删除成功");
    }

}
