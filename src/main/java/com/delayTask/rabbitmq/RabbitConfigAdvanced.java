package com.delayTask.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.delayTask.rabbitmq.RabbitmqConstants.*;

/**
 * @author zdh
 */
@Configuration
public class RabbitConfigAdvanced {

   //--------------------EXCHANGE--------------------------

   @Bean(ORDER_DELAYED_EXCHANGE)
   public CustomExchange orderExchange()
   {
      Map<String, Object> arguments = new HashMap<>();
      //自定义交换机的类型
      arguments.put("x-delayed-type","direct");
      /*
       * 1.交换机的名称
       * 2.交换机的类型
       * 3.是否需要持久化
       * 4.是否需要自动删除
       * 5.其他的参数
       * */
      return new CustomExchange(ORDER_DELAYED_EXCHANGE,
              //延迟交换机类型
              "x-delayed-message",
              false,false,arguments);
   }

   //--------------------QUEUE--------------------------

   @Bean(ORDER_DELAYED_QUEUE)
   public Queue orderQueue(){
      return QueueBuilder.nonDurable(ORDER_DELAYED_QUEUE).build();
   }

   //--------------------bind--------------------------

   @Bean
   public Binding orderDelayedBinding(@Qualifier(ORDER_DELAYED_QUEUE) Queue delayedQueue,
                                 @Qualifier(ORDER_DELAYED_EXCHANGE) CustomExchange orderDelayedExchange){
      return BindingBuilder.bind(delayedQueue).to(orderDelayedExchange).with(ORDER_DELAYED_ROUTE_KEY).noargs();
   }

}