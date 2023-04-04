package com.example.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.Result;
import com.example.dto.OrdersDto;
import com.example.dto.ProductDto;
import com.example.entity.OrdersEntity;
import com.example.entity.ProductEntity;
import com.example.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Slf4j
@Api(tags="秒杀管理")
@RestController
@RequestMapping("secKill")
public class SecKillController {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RedisScript redisScript;

    @Resource
    private ProductService productService;

    //    交换机
    private static final String CONFIRM_EXCHANGE="confirmExchange";

    //    RoutingKey
    private static final String CONFIRM_ROUTING_KEY="confirmRoutingKey";

    @PostMapping("/add")
    public Result<String> add(@RequestBody ProductDto productDto){

        log.info("添加秒杀商品:"+String.valueOf(productDto));
        productService.save(productDto);

        LambdaQueryWrapper<ProductEntity> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(ProductEntity::getImg,productDto.getImg());
        ProductEntity product=productService.getOne(queryWrapper);

        String qtKey="sk:"+product.getId()+":qt";
        String timeKey="sk:"+product.getId()+":time";


        if(productDto.getTime()>0)
            stringRedisTemplate.opsForValue().set(timeKey,"0",productDto.getTime(), TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(qtKey,productDto.getStock().toString());

        String key="product:"+productDto.getType();
        stringRedisTemplate.delete(key);

        return Result.success("添加成功");
    }

    @PostMapping("/change")
    public Result<String> change(@RequestBody ProductDto productDto) {
        log.info("修改普通商品:"+String.valueOf(productDto));
        String qtKey="sk:"+productDto.getId()+":qt";
        String timeKey="sk:"+productDto.getId()+":time";
        if(productDto.getTime()>0)
            stringRedisTemplate.opsForValue().set(timeKey,"0",productDto.getTime(), TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(qtKey,productDto.getStock().toString());

        try {
            //删除商品类型redis
            String key="product:"+productDto.getTypeId();
            stringRedisTemplate.delete(key);
            stringRedisTemplate.delete("product:1");
        }catch (Exception e){
            log.error(String.valueOf(e));
        }

//        修改数据库
        productDto.setTypeId(1L);
        productService.updateById(productDto);

        return Result.success("修改成功");
    }

    @PostMapping("/update")
    public Result<String> update(@RequestBody ProductDto productDto){
        log.info("修改秒杀商品:"+String.valueOf(productDto));
        String qtKey="sk:"+productDto.getId()+":qt";
        String timeKey="sk:"+productDto.getId()+":time";

        Boolean aBoolean = stringRedisTemplate.hasKey(timeKey);
//            是否开始秒杀
        if(!aBoolean){
//            是否已经秒杀完毕
            if(Integer.parseInt(stringRedisTemplate.opsForValue().get(qtKey))>0)
                return Result.error("商品秒杀中，修改失败！");
        }

        if(productDto.getTime()>0)
            stringRedisTemplate.opsForValue().set(timeKey,"0",productDto.getTime(), TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(qtKey,productDto.getStock().toString());

//        修改数据库
        productService.updateById(productDto);

        stringRedisTemplate.delete("product:1");

        //若修改商品类型，则删除该类型redis
        if(productDto.getTypeId()!=null&&productDto.getTypeId()!=1){
            String key="product:"+productDto.getTypeId();
            stringRedisTemplate.delete(key);
            stringRedisTemplate.delete(qtKey);
        }

        return Result.success("修改成功");
    }

    @ApiOperation(value = "开始秒杀")
    @PostMapping
    public  Result<OrdersEntity> secKill(@RequestBody OrdersDto orders){

        String timeKey="sk:"+orders.getProductId()+":time";
        Boolean aBoolean = stringRedisTemplate.hasKey(timeKey);
        if(aBoolean )
//            是否开始抢购
            return Result.error("抢购未开始！");

//        抢购商品库存key
        String qtKey="sk:"+orders.getProductId()+":qt";

//        该商品已经抢购过用户key
        String userKey="sk:"+orders.getProductId()+":users";
        Boolean member = stringRedisTemplate.opsForSet().isMember(userKey, String.valueOf(orders.getUserId()));
        if(member)
//            是否抢购过该商品
            return Result.error("抢购失败!请勿重复抢购!");

//        Lua脚本操作redis
        Long stock = (Long) stringRedisTemplate.execute(redisScript, Collections.singletonList(qtKey));

        if(stock==0){
            return Result.error("未查到该商品或商品已售空!");
        }

        stringRedisTemplate.opsForSet().add(userKey, String.valueOf(orders.getUserId()));

        orders.setStatus(1);

//        使用rabbitmq操作数据库
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE,CONFIRM_ROUTING_KEY, JSONUtil.toJsonStr(orders));

        return Result.success(orders);
    }

    @DeleteMapping
    public Result<String> delete(@RequestBody ProductDto productDto) {
        log.info("取消秒杀商品:" + String.valueOf(productDto));
        String qtKey = "sk:" + productDto.getId() + ":qt";
        String userKey="sk:"+ productDto.getId()+":users";
        stringRedisTemplate.delete(userKey);
        stringRedisTemplate.delete(qtKey);

        productService.removeById(productDto.getId());
        //若删除商品类型，则删除该类型redis
        String key="product:"+productDto.getTypeId();
        stringRedisTemplate.delete(key);
        return Result.success("删除成功");
    }
}
