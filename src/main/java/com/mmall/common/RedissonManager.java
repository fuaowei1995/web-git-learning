package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;



@Slf4j
@Component
public class RedissonManager {

    private static Config config = new Config();
    private static Redisson redisson = null;

    private static String  redisIp1 = PropertiesUtil.getProperty("redis1.ip");
    private static String redisPort1 = PropertiesUtil.getProperty("redis1.port");



    static {
            try {
                config.useSingleServer().setAddress(new StringBuilder().append(redisIp1).append(":").append(redisPort1).toString());
                redisson = (Redisson) Redisson.create(config);
                log.info("初始化Redisson完成");
            } catch (Exception e) {
                log.info(" init Redissson error",e);
        }

    }

    public Redisson getRedisson(){
        return this.redisson;
    }

}
