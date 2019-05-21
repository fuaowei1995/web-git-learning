package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {
        private static JedisPool pool;
        private static Integer maxTotol = Integer.parseInt(PropertiesUtil.getProperty("jedis.max.total","20"));
        private static Integer maxIdle =Integer.parseInt(PropertiesUtil.getProperty("jedis.max.idle","20")) ;
        private static Integer minIdle =Integer.parseInt(PropertiesUtil.getProperty("jedis.min.idle","20")) ;
        private static boolean testOnBorrow =Boolean.parseBoolean(PropertiesUtil.getProperty("jedis.test.borrow","true")) ;
        private static boolean testOnReturn =Boolean.parseBoolean(PropertiesUtil.getProperty("jedis.test.return","true"));
        private static String  redisIp = PropertiesUtil.getProperty("redis.ip");
        private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));

        private static void initPool(){
                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxTotal(maxTotol);
                config.setMaxIdle(maxIdle);
                config.setMinIdle(minIdle);
                config.setTestOnBorrow(testOnBorrow);
                config.setTestOnReturn(testOnReturn);
                config.setBlockWhenExhausted(true);//连接耗尽时候，是否阻塞，false抛出异常，true阻塞直到超时

                pool = new JedisPool(config,redisIp,redisPort,1000*2);
        }
        static{
                initPool();
        }

        public static Jedis getResource(){
                return pool.getResource();
        }

        public static void returnResource(Jedis jedis){

                pool.returnResource(jedis);

        }

        public static void returnBrokenResource(Jedis jedis){

                pool.returnBrokenResource(jedis);

        }

        public static void main(String[] args) {
                Jedis jedis = pool.getResource();
                jedis.set("akey","avalue");
                returnResource(jedis);

                pool.destroy();//临时调用，销毁连接
                System.out.println("jedis pool connect end");
        }
}
