package com.delayTask;

/**
 * @author 大忽悠
 * @create 2022/9/18 17:35
 */
public interface DelayTaskQueue<T,R> {

    /**
     *  <p>
     *      生成一个延迟任务加入延迟队列中去
     *  </p>
     * @param delayTaskEvent
     * @return 可以定位此次延迟任务的标记
     */
     T produce(DelayTaskEvent delayTaskEvent);

    /**
     * 处理到期的延迟任务
     */
     void consume(R taskId);

    /**
     * <p>
     *     取消taskId对应的延迟任务
     * </p>
     * @param taskId 延迟任务标记
     */
     void cancel(T taskId);
}
