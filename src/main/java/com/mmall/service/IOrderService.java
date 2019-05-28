package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;

import java.util.Map;

public interface IOrderService {

    ServerResponse pay(Long orderNo, Integer userId, String path);

    ServerResponse alipayCallback(Map<String,String> params);

    ServerResponse createOrder(Integer userId,Integer shippingId);

    ServerResponse cancel(Integer userId,Long orderNo);

    ServerResponse getOrderCartProduct(Integer userId);

    ServerResponse getOrderDetail(Integer userId,Long orderNo);

    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);

    ServerResponse manageList(int pageNum,int pageSize);

    ServerResponse manageDetail(Long orderNo);

    ServerResponse manageSearch(Long orderNo,int pageNum,int pageSize);

    ServerResponse manageSendGoods(Long orderNo);

    void closeOrder(int hour);
}
