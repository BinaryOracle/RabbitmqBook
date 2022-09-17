package com.delayTask.delayQueue;

import com.delayTask.domain.Order;

import java.util.concurrent.TimeUnit;

/**
 * @author 大忽悠
 * @create 2022/9/17 11:18
 */
public class OrderDelayFactory {
    /**
     * @param username 用户名
     * @param price 订单价格
     * @param commodity 商品名字
     * @param delayTime 延迟时间 -- 单位为秒
     */
    public static OrderDelayObject newOrderDelay(String username,String commodity,Double price,Long delayTime){
        return new OrderDelayObject(TimeUnit.SECONDS.toMillis(delayTime),Order.builder().id(System.currentTimeMillis()).commodityName(commodity).userName(username).price(price).build());
    }
}
