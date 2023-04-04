package com.example.controller.interceptor;

import com.example.common.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class InterceptorController implements HandlerInterceptor {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String employeeId = request.getHeader("EAuthorization");

        String userId = request.getHeader("Authorization");

        boolean matchEmployee = PATH_MATCHER.match("/employee/**", request.getRequestURI());

        boolean matchUser = PATH_MATCHER.match("/product/typeId", request.getRequestURI());

        if (!employeeId.equals("null") && matchEmployee) {
            log.info("员工: {} 发起请求:{}",employeeId,request.getRequestURI());
            return true;
        }else if(!userId.equals("null") && matchUser) {
            log.info("用户: {} 发起请求:{}",userId,request.getRequestURI());
            return true;
        }
        else {
            if(matchEmployee){
                log.info("{},请先登录后台账号",request.getRequestURI());
                throw new CustomException("请先登录后台账号");
            }
            if(matchUser){

                log.info("{},请先登录账号",request.getRequestURI());
                throw new CustomException("请先登录");
            }
            if(PATH_MATCHER.match("/address/list", request.getRequestURI()) ||PATH_MATCHER.match("/orders/user/info", request.getRequestURI())){
                if(!userId.equals("null")){
                    log.info("用户: {} 发起请求:{}",userId,request.getRequestURI());
                    return true;
                }
                log.info("{},请先登录账号",request.getRequestURI());
                throw new CustomException("请先登录");
            }
            if(employeeId.equals("null") && PATH_MATCHER.match("/user/page", request.getRequestURI())){
                log.info("{},请先登录后台账号",request.getRequestURI());
                throw new CustomException("请先登录后台账号");
            }
            log.info("员工: {} 用户: {} 发起请求:{}",employeeId,userId,request.getRequestURI());
            return true;
        }
    }
}
