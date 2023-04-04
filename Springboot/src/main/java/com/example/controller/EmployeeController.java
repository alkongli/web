package com.example.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.CustomException;
import com.example.common.Result;
import com.example.entity.OrdersEntity;
import com.example.entity.UserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import com.example.entity.EmployeeEntity;
import com.example.service.EmployeeService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 员工信息
 *
 * @author z
 * @email 2257957360@qq.com
 * @date 2022-12-16 18:58:13
 */
@Api(tags="员工管理")
@RestController
@RequestMapping("employee")
public class EmployeeController {
    @Resource
    private EmployeeService employeeService;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize,String username){
//        构造分页构造器
        Page<EmployeeEntity> pageInfo=new Page<>(page,pageSize);

        LambdaQueryWrapper<EmployeeEntity> queryWrapper=new LambdaQueryWrapper();

        if(username!=null)
            queryWrapper.likeRight(EmployeeEntity::getUsername,username);

//        添加排序
        queryWrapper.orderByAsc(EmployeeEntity::getCreateTime);
//        执行查询
        employeeService.page(pageInfo,queryWrapper);

        return Result.success(pageInfo);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public Result<EmployeeEntity> info(@PathVariable("id") Long id){
		EmployeeEntity employee = employeeService.getById(id);

        return Result.success(employee);
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<EmployeeEntity> login(HttpServletRequest request,@RequestBody EmployeeEntity employee){

        //对密码进行MD5加密
        String password=employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<EmployeeEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EmployeeEntity::getUsername,employee.getUsername()).eq(EmployeeEntity::getPassword,password);
        EmployeeEntity one = employeeService.getOne(queryWrapper);
        if(one==null)
            return Result.error("账号或密码错误");
        request.getSession().setAttribute("employeeId",one.getId());
        return Result.success(one);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Result<String> save(@RequestBody EmployeeEntity employee){
        //对密码进行MD5加密
        String password=employee.getPassword();
        employee.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));

		employeeService.save(employee);

        return Result.success("保存成功");
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改员工账号")
    @PutMapping
    public Result<String> update(@RequestBody EmployeeEntity employee){

        if(employee.getPassword()!=null){
            //对密码进行MD5加密
            String password=employee.getPassword();
            employee.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        }
        employeeService.updateById(employee);
        return Result.success("修改成功");
    }

    /**
     * 删除
     */
    @DeleteMapping
    public Result<String> delete(Long id){
        employeeService.removeById(id);
        return Result.success("删除成功");
    }

}
