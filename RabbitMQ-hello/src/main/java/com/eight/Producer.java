package com.eight;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.utils.RabbitMqUtils;

import java.nio.charset.StandardCharsets;

/*
 * 死信队列之生产者代码
 *
 * */
public class Producer {

    //普通交换机的名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        for (int i = 0; i < 10; i++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null, message.getBytes(StandardCharsets.UTF_8));
        }
    }
}

