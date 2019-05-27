package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

public class RedisShardedPool {
    private static ShardedJedisPool pool;
    private static Integer maxTotol = Integer.parseInt(PropertiesUtil.getProperty("jedis.max.total","20"));
    private static Integer maxIdle =Integer.parseInt(PropertiesUtil.getProperty("jedis.max.idle","20")) ;
    private static Integer minIdle =Integer.parseInt(PropertiesUtil.getProperty("jedis.min.idle","20")) ;
    private static boolean testOnBorrow =Boolean.parseBoolean(PropertiesUtil.getProperty("jedis.test.borrow","true")) ;
    private static boolean testOnReturn =Boolean.parseBoolean(PropertiesUtil.getProperty("jedis.test.return","true"));
    private static String  redisIp1 = PropertiesUtil.getProperty("redis1.ip");
    private static Integer redisPort1 = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));
    private static String  redisIp2 = PropertiesUtil.getProperty("redis2.ip");
    private static Integer redisPort2 = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));

    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotol);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setBlockWhenExhausted(true);//连接耗尽时候，是否阻塞，false抛出异常，true阻塞直到超时

        JedisShardInfo info1 = new JedisShardInfo(redisIp1,redisPort1,1000*2);
        JedisShardInfo info2 = new JedisShardInfo(redisIp2,redisPort2,1000*2);

        List<JedisShardInfo> jedisShardInfoList = new ArrayList<>();
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);
        pool = new ShardedJedisPool(config,jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }
    static{
        initPool();
    }

    public static ShardedJedis getResource(){
        return pool.getResource();
    }

    public static void returnResource(ShardedJedis shardedJedis){

        pool.returnResource(shardedJedis);

    }

    public static void returnBrokenResource(ShardedJedis shardedJedis){

        pool.returnBrokenResource(shardedJedis);

    }

//        public static void main(String[] args) {
//                Jedis jedis = pool.getResource();
//                jedis.set("akey","avalue");
//                returnResource(jedis);
//
//                pool.destroy();//临时调用，销毁连接
//                System.out.println("jedis pool connect end");
//        }
}




