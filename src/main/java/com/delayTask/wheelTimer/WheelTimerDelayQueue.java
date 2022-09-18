package com.delayTask.wheelTimer;

import com.delayTask.DelayTaskEvent;
import com.delayTask.DelayTaskQueue;
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
public class WheelTimerDelayQueue implements DelayTaskQueue<Timeout,DelayTaskEvent> {
    /**
     * 处理订单任务的线程池
     */
    private final ExecutorService THREAD_POOL= Executors.newCachedThreadPool();

    /**
     * 时间轮
     */
    private HashedWheelTimer wheelTimer;

    /**
     * 生产一个时间轮,默认的bucket数量为512个
     */
    public WheelTimerDelayQueue(Long duration){
        wheelTimer=new HashedWheelTimer(duration, TimeUnit.MILLISECONDS, 512);
    }

    /**
     * <p>
     * 生成一个延迟任务加入延迟队列中去
     * </p>
     *
     * @param delayTaskEvent
     * @return 可以定位此次延迟任务的标记
     */
    @Override
    public Timeout produce(DelayTaskEvent delayTaskEvent) {
        //延迟任务,延迟时间,时间单位
        return wheelTimer.newTimeout(delayTask -> {
            consume(delayTaskEvent);
        }, delayTaskEvent.getDelay(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
    }

    /**
     * 处理到期的延迟任务
     *
     * @param taskId
     */
    @Override
    public void consume(DelayTaskEvent taskId) {
        taskId.handleDelayEvent();
    }

    /**
     * <p>
     * 取消taskId对应的延迟任务
     * </p>
     *
     * @param taskId 延迟任务标记
     */
    @Override
    public void cancel(Timeout taskId) {
          taskId.cancel();
    }
}
