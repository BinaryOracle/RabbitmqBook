package com.delayTask.rabbitmq;

import com.delayTask.DelayTaskEvent;
import com.delayTask.DelayTaskQueue;
import com.delayTask.delayQueue.OrderDelayEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

import static com.delayTask.rabbitmq.RabbitmqConstants.*;


/**
 * @author zdh
 */
@Data
@Builder
@Slf4j
@Component
@RequiredArgsConstructor
public class MqDelayQueue implements DelayTaskQueue<OrderDelayEvent,OrderDelayEvent> {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper=new ObjectMapper();
    /**
     * 存放被取消的延迟任务集合
     */
    private final Set<Long> cancelDelayTask=new ConcurrentSkipListSet<>();

    /**
     * <p>
     * 生成一个延迟任务加入延迟队列中去
     * </p>
     *
     * @param delayTaskEvent
     * @return 可以定位此次延迟任务的标记
     */
    @Override
    public OrderDelayEvent produce(DelayTaskEvent delayTaskEvent) {
        try {
            rabbitTemplate.convertAndSend(ORDER_EXCHANGE,ORDER_ROUTE_KEY,objectMapper.writeValueAsString(delayTaskEvent),msg-> {
                msg.getMessageProperties().setExpiration(String.valueOf(delayTaskEvent.getDelay(TimeUnit.MILLISECONDS)));
                return msg;
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
         return (OrderDelayEvent) delayTaskEvent;
    }

    /**
     * 处理到期的延迟任务
     *
     * @param taskId
     */
    @Override
    public void consume(OrderDelayEvent taskId) {}

    @RabbitListener(queues = ORDER_DEAD_QUEUE)
    public void consume(Message message, Channel channel) throws Exception {
        OrderDelayEvent orderDelayEvent = objectMapper.readValue(new String(message.getBody()), OrderDelayEvent.class);
        log.info("消息队列中接收到一条消息: {}",orderDelayEvent);
        //被取消的延迟任务,不再进行处理
        if(cancelDelayTask.contains(orderDelayEvent.getId())){
            cancelDelayTask.remove(orderDelayEvent.getId());
            log.info("当前任务已被客户提交: {}",orderDelayEvent);
            return;
        }
        orderDelayEvent.handleDelayEvent();
    }

    /**
     * <p>
     * 取消taskId对应的延迟任务
     * </p>
     *
     * @param taskId 延迟任务标记
     */
    @Override
    public void cancel(OrderDelayEvent taskId) {
         cancelDelayTask.add(taskId.getId());
         taskId.getOrder().submitOrder();
    }
}
