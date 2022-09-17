package com.dhy.wheelTimer;

import com.delayTask.delayQueue.OrderDelayEvent;
import com.delayTask.delayQueue.OrderDelayFactory;
import com.delayTask.wheelTimer.WheelTimerHelper;
import io.netty.util.Timeout;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author 大忽悠
 * @create 2022/9/17 17:19
 */
public class WheelTimerTest {
    @BeforeTest
    public void beforeTest(){
        WheelTimerHelper.newWheelTimer(100L);
    }

    @Test
    public void testWheelTimer() throws InterruptedException {
        OrderDelayEvent orderDelay =  OrderDelayFactory.newOrderDelay("大忽悠", "小风扇", 13.4, 10L);
        OrderDelayEvent orderDelay1 = OrderDelayFactory.newOrderDelay("小朋友", "冰箱", 3000.0, 20L);
        Timeout timeout = WheelTimerHelper.addNewTask(orderDelay);
        Timeout timeout1 = WheelTimerHelper.addNewTask(orderDelay1);

        //订单二在到期前成功结算,因此不需要取消
        orderDelay1.getOrder().submitOrder();
        //取消延迟任务二
        timeout1.cancel();

        //阻塞,防止程序结束
        Thread.sleep(TimeUnit.SECONDS.toMillis(100L));
    }
}
