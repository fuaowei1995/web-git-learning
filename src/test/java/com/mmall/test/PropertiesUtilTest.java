package com.mmall.test;

import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
@Slf4j
public class PropertiesUtilTest {


    private static Properties props;
    static {
        String filename = "mmall.properties";
        props = new Properties();
       try {
           props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(filename),
                   "UTF-8"));
       }catch (IOException e){
          log.info("读入文件错误",e);
       }
    }

    public static String getPropertity(String key){
        if (key == null){
            System.out.println("参数错误");
        }
        String  value = props.getProperty(key);
        System.out.println("查询值为:"+value);
        return value;
    }

    public static void main(String[] args) {
        PropertiesUtilTest propertiesUtilTest = new PropertiesUtilTest();
        propertiesUtilTest.getPropertity("ftp.server.ip");
        propertiesUtilTest.getPropertity("ftp.user");
        propertiesUtilTest.getPropertity("ftp.pass");
        propertiesUtilTest.getPropertity("ftp.server.http.prefix");
    }

}
