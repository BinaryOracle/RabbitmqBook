package com.delayTask;

import java.util.concurrent.Delayed;

/**
 * @author 大忽悠
 * @create 2022/9/17 17:07
 */
public interface DelayTaskEvent extends Delayed{
    /**
     * 如果需要对延迟任务事件进行排序,可以重写此方法
     */
    @Override
    default int compareTo(Delayed o){
        return -1;
    };

    /**
     * 延迟任务到期后,要如何处理
     */
    void handleDelayEvent();
}
