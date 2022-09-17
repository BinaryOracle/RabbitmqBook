package com.dhy.delayQueue;


import com.delayTask.DelayTaskEvent;
import com.delayTask.delayQueue.OrderDelayFactory;
import com.delayTask.delayQueue.OrderDelayEvent;
import com.delayTask.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DelayQueueTest {
    /**
     * 延迟队列
     */
    private final DelayQueue<DelayTaskEvent> delayQueue = new DelayQueue<>();

    /**
     * 开启线程不断轮询,看是否有延迟任务可以处理
     */
    @BeforeTest
    public void beforeTest() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                while (true) {
                    //阻塞直到获取到某个到时的延迟任务
                    DelayTaskEvent delayObject = delayQueue.take();
                    //处理到期的延迟任务
                    delayObject.handleDelayEvent();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 测试下单
     */
    @Test
    public void testOrder() throws InterruptedException {
        OrderDelayEvent orderDelay =  OrderDelayFactory.newOrderDelay("大忽悠", "小风扇", 13.4, 10L);
        delayQueue.add(orderDelay);

        OrderDelayEvent orderDelay1 = OrderDelayFactory.newOrderDelay("小朋友", "冰箱", 3000.0, 20L);
        delayQueue.add(orderDelay1);

        Thread.sleep(TimeUnit.SECONDS.toMillis(8L));

        orderDelay.getOrder().submitOrder();
        delayQueue.remove(orderDelay);

        //防止程序结束
        Thread.sleep(TimeUnit.MINUTES.toMillis(10L));
    }


}
