package com.delayTask.delayQueue;


import com.delayTask.DelayTaskEvent;
import com.delayTask.domain.Order;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延时订单任务
 *
 * @author zdh
 */
@ToString
@Data
@Slf4j
@NoArgsConstructor
public class OrderDelayEvent implements DelayTaskEvent, Serializable {

    /**
     * 延迟任务唯一标识: 这里默认为当前时间戳
     */
    private Long id;

    /**
     * 延时时间
     */
    private long delayTime;

    /**
     * 订单对象
     */
    private Order order;

    public OrderDelayEvent(long delayTime, Order order) {
        this.id = System.currentTimeMillis();
        //延时时间加上当前时间
        this.delayTime = System.currentTimeMillis() + delayTime;
        this.order = order;
    }


    /**
     * 延迟任务是否到期
     */
    @Override
    public long getDelay(TimeUnit unit) {
        long diff = delayTime - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    /**
     * 延时任务队列，按照延时时间元素排序，实现Comparable接口
     */
    @Override
    public int compareTo(Delayed obj) {
        return Long.compare(this.delayTime, ((OrderDelayEvent) obj).delayTime);
    }

    /**
     * 延迟任务到期后,要如何处理
     */
    @Override
    public void handleDelayEvent() {
        log.info("延迟任务信息如下: {}",this);
        order.cancelOrderByTimeEnd();
    }
} 