package com.mmall.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieUtil {
    private final static String COOKIE_DOMIN = "happymmall";
    private final static String COOKIE_NAME = "JSESSIONID";


    //bug 写入redis中的token和chrome浏览器的cookie不一致get_user_info时
    // +导致拿不到redis中的数据
    public static void writeLoginToken(HttpServletResponse response,String token){
        Cookie ck = new Cookie(COOKIE_NAME,token);
        // Cookie ck = new Cookie(COOKIE_NAME,token);
        ck.setDomain(COOKIE_DOMIN);
        ck.setHttpOnly(true);
        ck.setPath("/");
        ck.setMaxAge(60*60*24*365);//设置cookie存在时间不设置表示存储在内存中，只在当前界面有效，设置后写入硬盘，单位秒；
        log.info("write cookie cookieName:{} cookieValue:{}",ck.getName(),ck.getValue());
        response.addCookie(ck);
    }

    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cks = request.getCookies();
        if (cks != null) {
            for (Cookie ck : cks) {
                log.info("read cookie cookieName:{} cookieValue:{} ",ck.getName(),ck.getValue());
                if(StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    log.info("return CookieName:{} CookieValue:{}",ck.getName(),ck.getValue());
                    return ck.getValue();
                }
            }
        }
        return null;
    }

    public static void delLoginToken(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cks = request.getCookies();
        if (cks != null){
            for (Cookie ck : cks){
                log.info("read cookie cookieName:{} cookieValue:{}",ck.getName(),ck.getValue());
                if (StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    ck.setDomain(COOKIE_DOMIN);
                    ck.setHttpOnly(true);
                    ck.setPath("/");
                    ck.setMaxAge(0);//设置为0代表删除此cookie
                    log.info("del cookie CookieName:{} CookieValue:{}",ck.getName(),ck.getValue());
                    response.addCookie(ck);
                }
            }
        }
    }


}
