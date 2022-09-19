package com.dhy.mq;

import com.delayTask.DelayTaskMain;
import com.delayTask.delayQueue.OrderDelayEvent;
import com.delayTask.delayQueue.OrderDelayFactory;
import com.delayTask.rabbitmq.MqDelayQueue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author 大忽悠
 * @create 2022/9/19 9:51
 */
@SpringBootTest(classes = DelayTaskMain.class)
public class MqDelayQueueTest {
    @Resource
    private MqDelayQueue mqDelayQueue;

    @Test
    public void testMqDelayQueue() throws InterruptedException {
        OrderDelayEvent orderDelay =  OrderDelayFactory.newOrderDelay("大忽悠", "小风扇", 13.4, 15L);
        OrderDelayEvent orderDelay1 = OrderDelayFactory.newOrderDelay("小朋友", "冰箱", 3000.0, 3L);
        mqDelayQueue.produce(orderDelay);
        mqDelayQueue.produce(orderDelay1);

        //Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        //mqDelayQueue.cancel(orderDelay1);

        Thread.sleep(TimeUnit.SECONDS.toMillis(30L));
    }
}
