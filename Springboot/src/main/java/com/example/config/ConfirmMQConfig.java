package com.example.config;

import org.springframework.amqp.core.*;
        import org.springframework.beans.factory.annotation.Qualifier;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * 发布确认
 */
@Configuration
public class ConfirmMQConfig {
    //    交换机
    private static final String CONFIRM_EXCHANGE="confirmExchange";

    //    队列
    private static final String CONFIRM_QUEUE="confirmQueue";
    //    RoutingKey
    private static final String CONFIRM_ROUTING_KEY="confirmRoutingKey";

    //    备份交换机
    private static final String CONFIRM2_EXCHANGE="confirm2Exchange";
    //    备份队列
    private static final String CONFIRM2_QUEUE="confirm2Queue";

    //    死信交换机
    private static final String DEAD_EXCHANGE="deadExchange";

    //    死信队列
    private static final String DEAD_QUEUE="deadQueue";

    // 死信RoutingKey
    public static final String DEAD_ROUTING_KEY="deadRoutingKey";

    //    创建交换机
    @Bean
    public Exchange confirmExchange(){
//        withArgument添加备份交换机
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE).durable(true).alternate(CONFIRM2_EXCHANGE).build();
    }
    //    创建备份交换机
    @Bean
    public FanoutExchange confirm2Exchange(){
        return ExchangeBuilder.fanoutExchange(CONFIRM2_EXCHANGE).durable(true).build();
    }
//    创建死信交换机
    @Bean
    public Exchange deadExchange(){
        return ExchangeBuilder.directExchange(DEAD_EXCHANGE).durable(true).build();
    }

    //    创建队列
    @Bean
    public Queue confirmQueue(){
        return QueueBuilder.durable(CONFIRM_QUEUE).deadLetterExchange(DEAD_EXCHANGE).deadLetterRoutingKey(DEAD_ROUTING_KEY).build();
    }

    //    创建备份队列
    @Bean
    public Queue confirm2Queue(){
        return QueueBuilder.durable(CONFIRM2_QUEUE).build();
    }

    //    创建死信队列
    @Bean
    public Queue deadQueue(){
        return QueueBuilder.durable(DEAD_QUEUE).build();
    }

    //    绑定
    @Bean
    public Binding QueueExchange(@Qualifier("confirmQueue") Queue queue,@Qualifier("confirmExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(CONFIRM_ROUTING_KEY).noargs();
    }

    //    绑定备份
    @Bean
    public Binding Queue2Exchange2(@Qualifier("confirm2Queue") Queue queue,@Qualifier("confirm2Exchange") FanoutExchange exchange){
        return BindingBuilder.bind(queue).to(exchange);
    }

    //    绑定死信
    @Bean
    public Binding DeadQueueExchange(@Qualifier("deadQueue") Queue queue,@Qualifier("deadExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(DEAD_ROUTING_KEY).noargs();
    }

}
