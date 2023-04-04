package com.example.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * 全局异常处理
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 异常
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request,Exception ex){

        log.error("{}: {}： Exception\n{}",LocalDateTime.now(),request.getRequestURI(),ex.getMessage());

        if(ex.getMessage().contains("Failed to convert value of type")){

            return Result.error("参数类型错误!");
        }
        return Result.error("出错了！请联系管理员或稍后再试。");
    }


    /**
     * 请求方式不存在
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<String> exceptionHandler(HttpServletRequest request,HttpRequestMethodNotSupportedException ex){
        log.error("{}: {}： HttpRequestMethodNotSupportedException\n{}",LocalDateTime.now(),request.getRequestURI(),ex.getMessage());
        if(ex.getMessage().contains("not supported")){
            String []split=ex.getMessage().split(" ");
            String msg=split[2]+"请求不存在!";
            return Result.error(msg);
        }

        return Result.error("出错了！请联系管理员或稍后再试。");
    }


    /**
     * SQL异常处理
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(DataAccessException.class)
    public Result<String> exceptionHandler(HttpServletRequest request,SQLException ex){
        log.error("{}: {}： DataAccessException\n{}",LocalDateTime.now(),request.getRequestURI(),ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
            String []split=ex.getMessage().split(" ");
            String msg=split[2]+"已存在";
            return Result.error(msg);
        }
        if(ex.getMessage().contains("doesn't have a default value")){
//            String []split=ex.getMessage().split(" ");
            return Result.error("数据错误，请检查提交的数据！");
        }
        return Result.error("SQL出现未知错误");
    }

    /**
     * 自定义异常处理
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public Result<String> exceptionHandler(HttpServletRequest request,CustomException ex){
        if(ex.getMessage().equals("请先登录")||ex.getMessage().equals("请先登录后台账号")){
            return Result.error(ex.getMessage());
        }
        log.error("{}: {}： CustomException\n{}",LocalDateTime.now(),request.getRequestURI(),ex.getMessage());
        return Result.error(ex.getMessage());
    }


}
