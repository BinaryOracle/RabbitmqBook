package com.delayTask.zset;

import com.delayTask.DelayTaskEvent;
import com.delayTask.DelayTaskQueue;
import com.delayTask.delayQueue.OrderDelayEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author 大忽悠
 * @create 2022/9/18 17:32
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RedisOrderDelayQueue implements DelayTaskQueue<DelayTaskEvent,DelayTaskEvent>, InitializingBean {
    /**
     * key
     */
    private static final String ORDER_DELAY_TASK_KEY="delaytask:order";

    @Resource(name = "redisTemplate")
    private ZSetOperations<String,Object> zSet;

    private final PollTime pollTimeManager =new PollTime();

    private final ExecutorService threadPool=Executors.newFixedThreadPool(3);

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
        OrderDelayEvent orderDelayEvent = (OrderDelayEvent) delayTaskEvent;
        //订单对象作为value
        long delayTime = orderDelayEvent.getDelay(TimeUnit.MILLISECONDS);
        //当前时间+订单延迟时间作为score
        long score = System.currentTimeMillis() + delayTime;
        //存入redis集合中
        zSet.add(ORDER_DELAY_TASK_KEY,orderDelayEvent,score);
        //轮询参数调整
        pollTimeManager.addDelayTask(delayTaskEvent.getDelay(TimeUnit.MILLISECONDS));
        return delayTaskEvent;
    }

    /**
     * 处理到期的延迟任务
     */
    @Override
    public void consume(DelayTaskEvent delayTaskEvent) {
        delayTaskEvent.handleDelayEvent();
        zSet.remove(ORDER_DELAY_TASK_KEY,delayTaskEvent);
    }

    /**
     * 查询redis,看是否有延迟任务到期
     */
    private void consume() {
        //查询出到期时间在当前时间之前的所有任务
        Set<ZSetOperations.TypedTuple<Object>> expiredTasks = zSet.rangeByScoreWithScores(ORDER_DELAY_TASK_KEY, 0, System.currentTimeMillis());
        //存在到期的延迟任务
        if (!CollectionUtils.isEmpty(expiredTasks)) {
            //挨个处理每个过期任务
            for (ZSetOperations.TypedTuple<Object> expiredTask : expiredTasks) {
                consume((DelayTaskEvent) expiredTask.getValue());
                //轮询参数调整
                pollTimeManager.removeDelayTask();
            }
        }
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
        zSet.remove(ORDER_DELAY_TASK_KEY,taskId);
        ((OrderDelayEvent) taskId).getOrder().submitOrder();
        pollTimeManager.removeDelayTask();
    }

    /**
     * Bean对象初始化好之后,就开始不断轮询,处理延迟任务
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Executors.newSingleThreadExecutor().execute(()->{
              while(true){
                  threadPool.submit(()->{
                      consume();
                  });
                  //睡眠指定的时间
                  try {
                      Thread.sleep(this.pollTimeManager.getPollTime());
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }
        });
    }

    /**
     * 计算轮询时间
     */
    public static class PollTime{
        private Long taskNum=0L;
        private Long delayTimeSum=0L;

        /**
         * @return 轮询时间默认为500毫秒
         */
        public Long getPollTime(){
            return 500L;
        }

        public synchronized void addDelayTask(Long delayTime){

        }

        public synchronized void removeDelayTask(){

        }
    }
}

