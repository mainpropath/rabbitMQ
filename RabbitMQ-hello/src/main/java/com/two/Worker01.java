package com.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Worker01 {
    // 队列名称
    public static final String QUEUE_NAME = "hello";
    // 接受消息
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        // 接受消息参数
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接受到的消息：" + new String(message.getBody()));
        };
        // 取消消费参数
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag + "消费者取消消费借口回调逻辑");
        };
        // 消息的接受
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
        System.out.println("C1接收消息~");
    }
}

