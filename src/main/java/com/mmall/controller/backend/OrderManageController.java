package com.mmall.controller.backend;


import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/order")
public class OrderManageController {

    @Autowired
    IOrderService iOrderService;

    @Autowired
    IUserService iUserService;

    @RequestMapping("order_list.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderList(HttpServletRequest httpServletRequest, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        //User user = (User)session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        User user = JsonUtil.string2Object(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        if(iUserService.checkRoleAdmin(user).isSuccess()){
            //填充业务逻辑
            return iOrderService.manageList(pageNum,pageSize);

        }
        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限！");
    }

    @RequestMapping("order_detail.do")
    @ResponseBody
    public ServerResponse orderDetail(HttpServletRequest httpServletRequest,Long orderNo){
       // User user = (User)session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        User user = JsonUtil.string2Object(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        if(iUserService.checkRoleAdmin(user).isSuccess()){
            //填充业务逻辑
            return iOrderService.manageDetail(orderNo);

        }
        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限！");
    }

    @RequestMapping("order_search.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderSearch(HttpServletRequest httpServletRequest,Long orderNo,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                      @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        //User user = (User)session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        User user = JsonUtil.string2Object(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        if(iUserService.checkRoleAdmin(user).isSuccess()){
            //填充业务逻辑
            return iOrderService.manageSearch(orderNo,pageNum,pageSize);

        }
        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限！");
    }

    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerResponse sendGoods(HttpServletRequest httpServletRequest,Long orderNo){
        //User user = (User)session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        User user = JsonUtil.string2Object(RedisShardedPoolUtil.get(loginToken),User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        if(iUserService.checkRoleAdmin(user).isSuccess()){
            //填充业务逻辑
            return iOrderService.manageSendGoods(orderNo);
        }
        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限！");
    }



}
