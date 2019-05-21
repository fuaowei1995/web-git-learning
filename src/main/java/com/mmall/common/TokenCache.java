package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.concurrent.TimeUnit;
@Slf4j
public class TokenCache {


    public static final String TOKEN_PREFIX = "token_";

    private static LoadingCache<String,String>localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS).
            build(new CacheLoader<String, String>() {
                //默认数据加载实现
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void setKey(String key, String value){
        localCache.put(key,value);
    }

    public static String getKey(String key){

        try{
        String value = localCache.get(key);
            if("".equals(value)){
                return null;
            }
            return value;
        }catch (Exception e){
           log.error("localCache get error",e);
            return null;
        }

    }
}
