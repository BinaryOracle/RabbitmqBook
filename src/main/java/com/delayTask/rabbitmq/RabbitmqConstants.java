package com.delayTask.rabbitmq;

/**
 * @author zdh
 */
public class RabbitmqConstants {

    //--------------------EXCHANGE--------------------------

    public static final String ORDER_EXCHANGE = "orderExchange";
    public static final String ORDER_DEAD_EXCHANGE = "orderDeadExchange";
    public static final String ORDER_DELAYED_EXCHANGE = "orderDelayedDeadExchange";

    //--------------------QUEUE--------------------------

    public static final String ORDER_QUEUE = "orderQueue";
    public static final String ORDER_DEAD_QUEUE = "orderDeadQueue";
    public static final String ORDER_DELAYED_QUEUE = "orderDelayedQueue";

    //------------------ROUTE_KEY-----------------------------

    public static final String ORDER_ROUTE_KEY="orderKey";
    public static final String ORDER_DEAD_ROUTE_KEY="orderDeadKey";
    public static final String ORDER_DELAYED_ROUTE_KEY="orderDelayedKey";
}