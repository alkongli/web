package com.example.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.OrdersEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import com.example.entity.UserEntity;
import com.example.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 用户表
 *
 * @author z
 * @email 2257957360@qq.com
 * @date 2022-12-16 18:58:13
 */
@RestController
@RequestMapping("user")
@Slf4j
@Api(tags="用户管理")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize,String name){
//        构造分页构造器
        Page<UserEntity> pageInfo=new Page<>(page,pageSize);

        LambdaQueryWrapper<UserEntity> queryWrapper=new LambdaQueryWrapper();

        if(name!=null)
            queryWrapper.likeRight(UserEntity::getName,name);

//        添加排序
        queryWrapper.orderByAsc(UserEntity::getCreateTime);
//        执行查询
        userService.page(pageInfo,queryWrapper);

        return Result.success(pageInfo);
    }


    /**
     * 信息
     */
    @GetMapping("/info")
    public Result<UserEntity> info(Long id,String phone){
        if(id!=null)
            return Result.success(userService.getById(id));
        if(phone!=null){
            LambdaQueryWrapper<UserEntity> queryWrapper=new LambdaQueryWrapper();
            queryWrapper.eq(UserEntity::getPhone,phone);
            return Result.success(userService.getOne(queryWrapper));
        }
        return Result.error("获取失败");
    }

    /**
     * 获取验证码
     * @param user
     * @return
     */
    @ApiOperation(value = "发送验证码")
    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody UserEntity user){
        String key="code:"+user.getPhone();

        if(StringUtils.isNotEmpty(user.getPhone())){
            //        使用hutool生成6位验证码
            String code= RandomUtil.randomString(6);
            log.info(code);
//        将验证码保存到redis 3分钟
            stringRedisTemplate.opsForValue().set(key,code,3, TimeUnit.MINUTES);

            return Result.success("获取验证码成功，验证码有效期为三分钟");
        }
        return Result.error("手机号为空");
    }

    /**
     * 登录
     */
    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public Result<UserEntity> login(HttpServletRequest request,@RequestBody UserEntity user){
        String key="code:"+user.getPhone();
        String s = stringRedisTemplate.opsForValue().get(key);
        if (s==null){
            return Result.error("未获取验证码，或验证码已过期，请重新获取验证码。");
        }
        if(s.equals(user.getCode())){
            LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserEntity::getPhone,user.getPhone());
            UserEntity one = userService.getOne(queryWrapper);
            if(one==null){
                user.setName(s);
                userService.save(user);
                one = userService.getOne(queryWrapper);
            }
            if(one.getStatus()==1)
                return Result.error("账号已被封");

            //删除验证码
            stringRedisTemplate.delete(key);

            request.getSession().setAttribute("userId",one.getId());
            return Result.success(one);
        }else
            return Result.error("验证码错误");
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Result<String> save(@RequestBody UserEntity user){
		userService.save(user);

        return Result.success("保存成功");
    }

    /**
     * 修改
     */
    @PutMapping
    public Result<String> update(@RequestBody UserEntity user){
		userService.updateById(user);

        return Result.success("修改成功");
    }

    /**
     * 删除
     */
    @DeleteMapping
    public Result<String> delete(@RequestBody Long[] ids){
		userService.removeByIds(Arrays.asList(ids));

        return Result.success("删除成功");
    }

}
