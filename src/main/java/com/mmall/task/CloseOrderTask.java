package com.mmall.task;

import com.mmall.common.Const;
import com.mmall.common.RedisShardedPool;
import com.mmall.common.RedissonManager;
import com.mmall.pojo.Product;
import com.mmall.service.IOrderService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CloseOrderTask {
    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private RedissonManager redissonManager;

    @PreDestroy
    public void preDestroy(){
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        log.info("启动tomcat初始化删除分布式锁");
    }

    //@Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV1(){
        log.info("定时关闭订单开始");
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour"));
        //关闭订单业务逻辑
        iOrderService.closeOrder(hour);
       log.info("定时关闭订单关闭");
    }

   // @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV2(){
        log.info("定时关闭订单开始");
        //关闭订单业务逻辑
        //添加分布式锁
        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.time.out"));
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+lockTimeout));
        if (setnxResult != null && setnxResult.intValue() ==1){
            //说明设置分布式锁成功，执行关单业务
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }else{
            log.info("未获得分布式锁：{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }
        log.info("定时关闭订单关闭");
    }


    //@Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV3(){
        log.info("定时关闭订单开始");
        //关闭订单业务逻辑
        //添加分布式锁
        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.time.out"));
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+lockTimeout));
        if (setnxResult != null && setnxResult.intValue() ==1){
            //说明设置分布式锁成功，执行关单业务
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }else{
            String lockValueStr = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            if (lockValueStr != null && System.currentTimeMillis() > Long.parseLong(lockValueStr)){
                String getSetResult = RedisShardedPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis()+lockTimeout));
                if ( getSetResult == null || ( getSetResult != null && StringUtils.equals(getSetResult,String.valueOf(System.currentTimeMillis()+lockTimeout)))){
                    closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }else{
                    log.info("未获得分布式锁：{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }
            }else{
                log.info("未获得分布式锁：{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            }
        }
        log.info("定时关闭订单关闭");
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV4(){
        RLock lock = redissonManager.getRedisson().getLock(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        boolean getlock = false;
        try {
            getlock=lock.tryLock(0,50, TimeUnit.SECONDS);
            if (getlock){
                log.info("Redisson获取到分布式锁{},TreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
                int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour"));
                //iOrderService.closeOrder(hour);
            } else {
                log.info("Redisson获取到分布式锁{},TreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.info("Redisson获取分布式锁失败",e);
        } finally {
            if (!getlock){
                return ;
            }
            lock.unlock();
            log.info("Redisson释放分布式锁{},ThreadName{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
        }

    }

    private void closeOrder(String lockName){
        RedisShardedPoolUtil.expire(lockName,50);
        log.info("获取{} TreadName {}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour"));
        //iOrderService.closeOrder(hour);
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        log.info("释放{} TreadName {}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
    }

}
