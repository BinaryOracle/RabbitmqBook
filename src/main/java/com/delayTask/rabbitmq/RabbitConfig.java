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
public class RabbitConfig {

   //--------------------EXCHANGE--------------------------

   @Bean(ORDER_EXCHANGE)
   public DirectExchange orderExchange()
   {
      return new DirectExchange(ORDER_EXCHANGE);
   }

   @Bean(ORDER_DEAD_EXCHANGE)
   public DirectExchange orderDeadExchange()
   {
      return new DirectExchange(ORDER_DEAD_EXCHANGE);
   }

   //--------------------QUEUE--------------------------

   @Bean(ORDER_QUEUE)
   public Queue orderQueue(){
      Map<String, Object> arguments = new HashMap<>(3);
      //设置死信交换机
      arguments.put("x-dead-letter-exchange",ORDER_DEAD_EXCHANGE);
      //设置死信Routing-key
      arguments.put("x-dead-letter-routing-key",ORDER_DEAD_ROUTE_KEY);
      return QueueBuilder.nonDurable(ORDER_QUEUE).withArguments(arguments).build();
   }

   @Bean(ORDER_DEAD_QUEUE)
   public Queue orderDeadQueue(){
      return QueueBuilder.nonDurable(ORDER_DEAD_QUEUE).build();
   }

   //--------------------bind--------------------------

   @Bean
   public Binding orderBinding(@Qualifier(ORDER_QUEUE) Queue orderQueue,
                                 @Qualifier(ORDER_EXCHANGE) DirectExchange orderExchange){
      return BindingBuilder.bind(orderQueue).to(orderExchange).with(ORDER_ROUTE_KEY);
   }


   @Bean
   public Binding deadOrderBinding(@Qualifier(ORDER_DEAD_QUEUE) Queue orderDeadQueue,
                                 @Qualifier(ORDER_DEAD_EXCHANGE) DirectExchange orderDeadExchange){
      return BindingBuilder.bind(orderDeadQueue).to(orderDeadExchange).with(ORDER_DEAD_ROUTE_KEY);
   }
}