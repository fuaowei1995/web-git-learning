package com.mmall.util;

import com.google.common.collect.Lists;
import com.mmall.pojo.User;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();
    static {

        //对象的所有字段端部列入
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);
        //取消默认转换timestamps形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);
        //忽略空Bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
        //所有日期格式统一为 yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));

        //忽略json字符串存在，但在java对象中不存在的情况
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);

    }

    public static <T>String obj2String(T obj){
        if (obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj: objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("parse object to String error",e);
            return null;
        }
    }

    public static <T>String obj2StringPretty(T obj){
        if (obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj: objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("parse object to String error",e);
            return null;
        }
    }



    public static <T> T string2Object(String str,Class<T>tClass){
        if (StringUtils.isEmpty(str) || tClass == null){
            return null;
        }
        try {
            return tClass.equals(String.class)? (T)str : objectMapper.readValue(str,tClass);
        } catch (IOException e) {
            log.info("String to Object error",e);
            return null;
        }
    }


    public static <T> T string2Obj(String str, TypeReference<T> typeReference){
        if (StringUtils.isEmpty(str) || typeReference == null){
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str,typeReference));
        } catch (IOException e) {
            log.info("String to Object error",e);
            return null;
        }
    }

    public static <T> T string2Obj(String str, Class<?>collectionClass,Class<?>elementClass){
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClass);
        try {
            return (T)(javaType.equals(String.class) ? str : objectMapper.readValue(str,javaType));
        } catch (IOException e) {
            log.info("String to Object error",e);
            return null;
        }
    }

//    public static void main(String[] args) {
//        User u1 = new User();
//        u1.setId(1);
//        u1.setUsername("user1");
//        User u2 = new User();
//        u2.setId(1);
//        u2.setUsername("user1");
//
//        String stru1 = JsonUtil.obj2String(u1);
//        String stru1Pretty = JsonUtil.obj2StringPretty(u1);
//        List<User> userList = Lists.newArrayList();
//        userList.add(u1);
//        userList.add(u2);
//        String struList = JsonUtil.obj2String(userList);
//
//        List<User>userList2 = JsonUtil.string2Obj(struList, new TypeReference<List<User>>() {});
//
//        List<User>userList3 = JsonUtil.string2Obj(struList,List.class,User.class);
//        User u3 =JsonUtil.string2Object(stru1,User.class);
//
//
//        log.info("program end");
//    }

}
