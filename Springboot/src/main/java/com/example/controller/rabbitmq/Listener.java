package com.example.controller.rabbitmq;

import cn.hutool.json.JSONUtil;
import com.example.common.Result;
import com.example.dto.OrdersDto;
import com.example.entity.OrdersEntity;
import com.example.service.OrdersService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Slf4j
public class Listener {
    //    队列
    private static final String CONFIRM_QUEUE="confirmQueue";

    //    备份队列
    private static final String CONFIRM2_QUEUE="confirm2Queue";

    //    死信队列
    private static final String DEAD_QUEUE="deadQueue";

    @Resource
    private OrdersService ordersService;


    @RabbitListener(queues = CONFIRM_QUEUE)
    public void Listener(Message message, Channel channel) throws IOException {
        String msg=new String(message.getBody());
        OrdersDto orders = JSONUtil.toBean(msg, OrdersDto.class);
        log.info("{}:成功获取来自队列 {} 的消息：{}", LocalDateTime.now(),CONFIRM_QUEUE,orders);
        try {
//            生成流水号
            orders.setId(Result.getId());
            ordersService.addKillOrders(orders);
        }catch (Exception e){
            log.info("{},消息处理报错:{}",msg,String.valueOf(e));
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }

    }

    @RabbitListener(queues = CONFIRM2_QUEUE)
    public void Listener2(Message message, Channel channel){
        String msg=new String(message.getBody());
        OrdersDto orders = JSONUtil.toBean(msg, OrdersDto.class);
        log.error("{}:备份队列收到消息,来自队列 {} 的消息：{}", LocalDateTime.now(),CONFIRM2_QUEUE, orders);
    }

    @RabbitListener(queues = DEAD_QUEUE)
    public void Listener3(Message message, Channel channel){
        String msg=new String(message.getBody());
        OrdersDto orders = JSONUtil.toBean(msg, OrdersDto.class);
        log.error("{}:死信队列收到消息,来自队列 {} 的消息：{}", LocalDateTime.now(),DEAD_QUEUE, orders);
    }
}
