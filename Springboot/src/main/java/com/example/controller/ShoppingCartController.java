package com.example.controller;

import java.util.Arrays;
import java.util.List;

import com.example.common.Result;

import com.example.dto.ShoppingCartDto;
import com.example.entity.ShoppingCartEntity;
import org.springframework.web.bind.annotation.*;

import com.example.service.ShoppingCartService;

import javax.annotation.Resource;


/**
 * 购物车
 *
 * @author z
 * @email 2257957360@qq.com
 * @date 2023-03-17 15:30:46
 */
@RestController
@RequestMapping("shoppingCart")
public class ShoppingCartController {
    @Resource
    private ShoppingCartService shoppingCartService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public Result<List<ShoppingCartDto>> list(Long userId){

        List<ShoppingCartDto> list = shoppingCartService.getList(userId);

        return Result.success(list);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Result<String> save(@RequestBody ShoppingCartEntity shoppingCart){
		shoppingCartService.save(shoppingCart);

        return Result.success("添加购物车成功");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public Result<String> delete(@RequestBody Long[] ids){
		shoppingCartService.removeByIds(Arrays.asList(ids));
        return Result.success("删除成功");
    }

}
