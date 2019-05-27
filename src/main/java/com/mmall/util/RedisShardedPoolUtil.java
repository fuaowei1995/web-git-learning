package com.mmall.util;

import com.mmall.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.util.Sharded;

@Slf4j
public class RedisShardedPoolUtil {


    //exTime 单位秒
    public static String setEx(String key,String value,int exTime){
        ShardedJedis jedis = null;
        String result = null;

        try{
            jedis = RedisShardedPool.getResource();
            result = jedis.setex(key,exTime,value);
        }catch (Exception e){
            log.info("setex key{},value{},error",key,value,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static Long expire(String key,int exTime){
        ShardedJedis jedis = null;
        Long result = null;

        try{
            jedis = RedisShardedPool.getResource();
            result = jedis.expire(key,exTime);
        }catch (Exception e){
            log.info("set expire key{},value{},error",key,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }
    public static String set(String key,String value){
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getResource();
            result = jedis.set(key,value);
        } catch (Exception e) {
            log.info("set key{} value{} error",key,value,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static String get(String key){
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getResource();
            result = jedis.get(key);
        } catch (Exception e) {
            log.info("get key{} value{} error",key,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static Long del(String key){
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getResource();
            result = jedis.del(key);
        } catch (Exception e) {
            log.info("get key{} value{} error",key,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }
//

}
