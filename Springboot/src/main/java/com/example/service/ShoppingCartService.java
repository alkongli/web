package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dto.ShoppingCartDto;
import com.example.entity.ShoppingCartEntity;

import java.util.List;
import java.util.Map;

/**
 * 购物车
 *
 * @author z
 * @email 2257957360@qq.com
 * @date 2023-03-17 15:30:46
 */
public interface ShoppingCartService extends IService<ShoppingCartEntity> {

    public List<ShoppingCartDto> getList(Long userId);

}

