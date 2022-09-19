package com.dhy.redis;

import com.delayTask.DelayTaskMain;
import com.delayTask.delayQueue.OrderDelayEvent;
import com.delayTask.delayQueue.OrderDelayFactory;
import com.delayTask.zset.RedisOrderDelayQueue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.concurrent.TimeUnit;

/**
 * @author 大忽悠
 * @create 2022/9/18 19:44
 */
@SpringBootTest(classes = DelayTaskMain.class)
public class RedisZSetTest {
    @Autowired
    private RedisOrderDelayQueue redisOrderDelayQueue;

    @Test
    public void testZSet() throws InterruptedException {
        OrderDelayEvent orderDelay =  OrderDelayFactory.newOrderDelay("大忽悠", "小风扇", 13.4, 10L);
        OrderDelayEvent orderDelay1 = OrderDelayFactory.newOrderDelay("小朋友", "冰箱", 3000.0, 20L);
        redisOrderDelayQueue.produce(orderDelay);
        redisOrderDelayQueue.produce(orderDelay1);

        Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        redisOrderDelayQueue.cancel(orderDelay1);

        Thread.sleep(TimeUnit.SECONDS.toMillis(30));
    }

}
