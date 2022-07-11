package com.two;

import com.rabbitmq.client.Channel;
import com.utils.RabbitMqUtils;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Task01 {
    // 队列名称
    public static final String QUEUE_NAME = "hello";

    // 发送大量消息
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        // 队列的声明
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 从控制台中输入消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("发送消息完成：" + message);
        }
    }
}

