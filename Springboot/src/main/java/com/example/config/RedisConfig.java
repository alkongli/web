package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

@Component
public class RedisConfig {

    @Bean
    private DefaultRedisScript<Long> script(){
        DefaultRedisScript<Long> redisScript= new DefaultRedisScript<>();
//        resources/stock.lua
//        redisScript.setLocation(new ClassPathResource("stock.lua"));

        String script="if (redis.call(\"exists\",KEYS[1])==1) then\n" +
                "    local stock=tonumber(redis.call(\"get\",KEYS[1]));\n" +
                "    if(stock>0) then\n" +
                "        redis.call(\"decr\",KEYS[1]);\n" +
                "        return stock;\n" +
                "    end\n" +
                "    return 0;\n" +
                "end\n" +
                "return 0;";

        redisScript.setScriptText(script);
        redisScript.setResultType(Long.class);
        return redisScript;
    }


}
