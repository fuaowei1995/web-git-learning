package com.mmall.controller.backend;


import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/order/")
public class OrderManageController {

    @Autowired
    IOrderService iOrderService;

    @Autowired
    IUserService iUserService;

    @RequestMapping("order_list.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
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
    public ServerResponse orderDetail(HttpSession session,Long orderNo){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
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
    public ServerResponse<PageInfo> orderSearch(HttpSession session,Long orderNo,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                      @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
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
    public ServerResponse sendGoods(HttpSession session,Long orderNo){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
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
