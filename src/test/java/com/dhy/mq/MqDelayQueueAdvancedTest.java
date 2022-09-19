package com.dhy.mq;

import com.delayTask.DelayTaskMain;
import com.delayTask.delayQueue.OrderDelayEvent;
import com.delayTask.delayQueue.OrderDelayFactory;
import com.delayTask.rabbitmq.MqDelayQueueAdvanced;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author 大忽悠
 * @create 2022/9/19 10:34
 */
@SpringBootTest(classes = DelayTaskMain.class)
public class MqDelayQueueAdvancedTest {
    @Resource
    private MqDelayQueueAdvanced mqDelayQueueAdvanced;

    @Test
    public void test() throws InterruptedException {
        OrderDelayEvent orderDelay =  OrderDelayFactory.newOrderDelay("大忽悠", "小风扇", 13.4, 15L);
        OrderDelayEvent orderDelay1 = OrderDelayFactory.newOrderDelay("小朋友", "冰箱", 3000.0, 10L);
        mqDelayQueueAdvanced.produce(orderDelay);
        mqDelayQueueAdvanced.produce(orderDelay1);

        Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        mqDelayQueueAdvanced.cancel(orderDelay1);

        Thread.sleep(TimeUnit.SECONDS.toMillis(30L));
    }
}
