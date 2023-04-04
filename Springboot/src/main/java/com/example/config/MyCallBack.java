package com.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback {


    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
//    注入方法
    public void in(){
        rabbitTemplate.setConfirmCallback(this);
    }

    /**
     * 交换机回调确认
     * @param correlationData   保存回调消息ID 等相关信息
     * @param b  交换机是否收到消息
     * @param s  如果接收失败表述失败原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
//        if(b)
//            log.info("消息接收成功!");
//        else {
//            log.info("发生错误:{}",s);
//        }
        if(!b)
            log.info("发生错误:{}",s);

    }

}
