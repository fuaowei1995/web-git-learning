package com.mmall.common;

import com.google.common.collect.Sets;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.Set;

public class Const {

    public static final String CURRENT_USER = "currentUser";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";

    public interface Role {
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;
    }

    public interface Cart {
        int CHECKED = 1;
        int UN_CHECKED = 0;

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public interface RedisCacheTime{
        int REDIS_SESSION_EXTIME = 60*30;//30 minutes
    }

    public interface ProductListOrderBy {
        Set<String> PRICA_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");
    }

    public enum ProductStatusEnum {
        ON_SALE("在线", 1);

        private String value;
        private int code;

        ProductStatusEnum(String value, int code) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public enum OrderStatusEnum {
        CANCEED(0, "已取消"),
        NO_PAY(10, "未支付"),
        PAID(20, "已支付"),
        SHIPPED(40, "已发货"),
        ORDER_SUCCESS(50, "订单已完成"),
        ORDER_CLOSE(60, "订单关闭");

        private int code;
        private String value;

        OrderStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }


        public int getCode() {
            return this.code;
        }

        public String getValue() {
            return this.value;
        }

        public static OrderStatusEnum codeof(int code){
            for(OrderStatusEnum orderStatusEnum :values()){
                if (orderStatusEnum.getCode() == code ){
                    return  orderStatusEnum ;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }
    }

    public interface AlipayCallBack {
        String TRADE_STATUS_WAIT_BUY_PAY = "WAIT_BUY_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";
        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

    public enum PayPlatformEnum{

        ALIPAY(1, "支付包");

        PayPlatformEnum( int code, String value){
            this.code = code;
            this.value = value;

    }
        private int code;
        private String value;
        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }



    }

    public enum PaymentTypeEnum{
        ON_LINE(1,"在线支付");
        PaymentTypeEnum( int code, String value){
            this.code = code;
            this.value = value;

        }
        private int code;
        private String value;
        public int getCode() {
            return code;
        }
        public String getValue() {
            return value;
        }

        public  static PaymentTypeEnum codeof(int code){
            for(PaymentTypeEnum paymentTypeEnum :values()){
                if (paymentTypeEnum.getCode() == code ){
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }

    }

}
