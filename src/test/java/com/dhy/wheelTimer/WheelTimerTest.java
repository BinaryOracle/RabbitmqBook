package com.dhy.wheelTimer;

import com.delayTask.DelayTaskEvent;
import com.delayTask.DelayTaskQueue;
import com.delayTask.delayQueue.OrderDelayEvent;
import com.delayTask.delayQueue.OrderDelayFactory;
import com.delayTask.wheelTimer.WheelTimerDelayQueue;
import io.netty.util.Timeout;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author 大忽悠
 * @create 2022/9/17 17:19
 */
public class WheelTimerTest {

    @Test
    public void testWheelTimer() throws InterruptedException {
        DelayTaskQueue<Timeout, DelayTaskEvent> delayTaskQueue=new WheelTimerDelayQueue(100L);
        OrderDelayEvent orderDelay =  OrderDelayFactory.newOrderDelay("大忽悠", "小风扇", 13.4, 10L);
        OrderDelayEvent orderDelay1 = OrderDelayFactory.newOrderDelay("小朋友", "冰箱", 3000.0, 20L);
        delayTaskQueue.produce(orderDelay);
        Timeout timeout1 = delayTaskQueue.produce(orderDelay1);

        //订单二在到期前成功结算,因此不需要取消
        orderDelay1.getOrder().submitOrder();
        //取消延迟任务二
        delayTaskQueue.cancel(timeout1);

        //阻塞,防止程序结束
        Thread.sleep(TimeUnit.SECONDS.toMillis(100L));
    }
}
