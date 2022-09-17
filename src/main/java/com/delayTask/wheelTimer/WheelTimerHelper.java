package com.delayTask.wheelTimer;

import com.delayTask.DelayTaskEvent;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <h>
 *     时间轮工厂
 * </h>
 * @author 大忽悠
 * @create 2022/9/17 17:17
 */
public class WheelTimerHelper {
    /**
     * 处理订单任务的线程池
     */
    private static final ExecutorService THREAD_POOL= Executors.newCachedThreadPool();

    /**
     * 时间轮
     */
    private static HashedWheelTimer wheelTimer;

    /**
     * 生产一个时间轮,默认的bucket数量为512个
     */
    public static HashedWheelTimer newWheelTimer(Long duration){
        wheelTimer=new HashedWheelTimer(duration, TimeUnit.MILLISECONDS, 512);
        return wheelTimer;
    }

    /**
     * @param delayTaskEvent 延迟任务事件
     */
    public static Timeout addNewTask(DelayTaskEvent delayTaskEvent){
        //延迟任务,延迟时间,时间单位
        return wheelTimer.newTimeout(delayTask -> {
            delayTaskEvent.handleDelayEvent();
        }, delayTaskEvent.getDelay(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
    }
}
