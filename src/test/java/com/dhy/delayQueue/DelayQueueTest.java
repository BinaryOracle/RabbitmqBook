package com.dhy.delayQueue;


import com.delayTask.DelayTaskEvent;
import com.delayTask.DelayTaskQueue;
import com.delayTask.delayQueue.DelayQueue;
import com.delayTask.delayQueue.OrderDelayFactory;
import com.delayTask.delayQueue.OrderDelayEvent;
import com.delayTask.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DelayQueueTest {
    /**
     * 测试下单
     */
    @Test
    public void testOrder() throws InterruptedException {
        DelayTaskQueue<DelayTaskEvent,DelayTaskEvent> delayTaskQueue=new DelayQueue();
        OrderDelayEvent orderDelay =  OrderDelayFactory.newOrderDelay("大忽悠", "小风扇", 13.4, 10L);
        delayTaskQueue.produce(orderDelay);

        OrderDelayEvent orderDelay1 = OrderDelayFactory.newOrderDelay("小朋友", "冰箱", 3000.0, 20L);
        delayTaskQueue.produce(orderDelay1);

        Thread.sleep(TimeUnit.SECONDS.toMillis(8L));

        delayTaskQueue.cancel(orderDelay);

        //防止程序结束
        Thread.sleep(TimeUnit.MINUTES.toMillis(10L));
    }


}
