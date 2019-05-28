package com.mmall.dao;

import com.mmall.pojo.Order;
import com.mmall.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectByUserIdAndOrderNo(@Param("userId") Integer userId, @Param("orderNo")Long orderNo);

    //List<OrderItem> getByOrderNoUserId(@Param("orderNo") Long orderNo,@Param("userId")Integer userId);

    Order selectByOrderNo(Long orderNo);

    List<Order> selectByUserId(@Param("UserId") Integer userId);

    List<Order>selectAllOrder();

    //二期新增
    List<Order>selectOrderStatusByCreateTime(@Param("status")Integer status,@Param("date")String date);

    void closeOrderByOrderId(Integer OrderId);

}