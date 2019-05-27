package com.mmall.controller.common.interceptor;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //请求controller中的方法
        HandlerMethod handlerMethod = (HandlerMethod) o;

        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();
        // 解析参数，具体参数key和value是多少
        StringBuffer requestParamBuffer = new StringBuffer();
        Map paramMap = httpServletRequest.getParameterMap();
        Iterator it = paramMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry entry = (Map.Entry)it.next();
            String mapKey = (String) entry.getKey();
            String mapValue = StringUtils.EMPTY;

            Object obj = entry.getValue();
            if (obj instanceof String[]){
                String[] str = (String[])obj;
                mapValue = Arrays.toString(str);
            }
            requestParamBuffer.append(mapKey).append("=").append(mapValue);
        }
        if (StringUtils.equals(className,"UserManageController")&&StringUtils.equals(methodName,"login")){
            log.info("拦截器拦截到请求： className {} methodName {}",className,methodName);//为登录请求，不打印参数防止密码泄露
            return true;
        }
        log.info("拦截器拦截到请求：className{} methodName{} requestParam{}",className,methodName,requestParamBuffer);

        User user = null;
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (loginToken != null){
            user = JsonUtil.string2Object(RedisShardedPoolUtil.get(loginToken),User.class);
        }
        if (user == null ||(user.getRole().intValue() != Const.Role.ROLE_ADMIN)){
            httpServletResponse.reset();
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            PrintWriter out = httpServletResponse.getWriter();
            if (user == null){
                if (StringUtils.equals(methodName,"ProductManageController")&&StringUtils.equals(className,"richtextImgUpload")){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success",false);
                    resultMap.put("msg","请登录管理员");
                    out.print(JsonUtil.obj2String(resultMap));
                }
                out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截，用户未登录")));
            }else{
                if (StringUtils.equals(methodName,"ProductManageController")&&StringUtils.equals(className,"richtextImgUpload")){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success",false);
                    resultMap.put("msg","无权限操作");
                    out.print(JsonUtil.obj2String(resultMap));
                }
                out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截，用户无权限")));
            }
            out.flush();
            out.close();
        }


        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
