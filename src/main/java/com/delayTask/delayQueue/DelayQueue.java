package com.delayTask.delayQueue;

import com.delayTask.DelayTaskEvent;
import com.delayTask.DelayTaskQueue;

import java.util.concurrent.Executors;

/**
 * @author 大忽悠
 * @create 2022/9/18 17:49
 */
public class DelayQueue implements DelayTaskQueue<DelayTaskEvent,DelayTaskEvent> {
    /**
     * 延迟队列
     */
    private final java.util.concurrent.DelayQueue<DelayTaskEvent> delayQueue = new java.util.concurrent.DelayQueue<>();

    public DelayQueue() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                while (true) {
                    //阻塞直到获取到某个到时的延迟任务
                    consume(delayQueue.take());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
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
    public DelayTaskEvent produce(DelayTaskEvent delayTaskEvent) {
        delayQueue.add(delayTaskEvent);
        return delayTaskEvent;
    }

    /**
     * 处理到期的延迟任务
     *
     * @param taskId
     */
    @Override
    public void consume(DelayTaskEvent taskId) {
        //处理到期的延迟任务
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
    public void cancel(DelayTaskEvent taskId) {
        delayQueue.remove(taskId);
    }
}
