package com.delayTask.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Order {
    Long id;
    String userName;
    String commodityName;
    Double price;


    public void cancelOrderByTimeEnd() {
        System.out.println("订单到时,自动取消中....");
        System.out.println("订单信息: "+toString());
    }

    public void submitOrder() {
        System.out.println("提单提交中...");
        System.out.println("订单信息: "+toString());
    }
}
