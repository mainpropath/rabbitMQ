package com.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.utils.RabbitMqUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ConfirmMessage {

    public static final int COUNT = 1000;

    public static void main(String[] args) throws Exception {
        //单个确认测试
//        publishMessageIndividually();
        //批量确认测试
//        publishMessageBatch();
        //异步确认测试
        publicMessageAsync();
    }

    //得到信道
    public static Channel getConfirmChannel(String queueName) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //队列的声明
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        return channel;
    }

    //单个发布确认
    public static void publishMessageIndividually() throws Exception {
        String queueName = UUID.randomUUID().toString();
        Channel channel = getConfirmChannel(queueName);
        //开始时间
        long start = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
            //单个消息马上进行发布确认
            boolean flag = channel.waitForConfirms();
            if (flag) System.out.println("消息发送成功~");
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("单个发布确认发送" + COUNT + "条信息所耗时间：" + (end - start) + "ms");
    }

    //批量信息确认
    public static void publishMessageBatch() throws Exception {
        String queueName = UUID.randomUUID().toString();
        Channel channel = getConfirmChannel(queueName);
        long start = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
            //每100条信息确认一次
            if (i + 1 % 100 == 0) channel.waitForConfirms();
        }
        long end = System.currentTimeMillis();
        System.out.println("批量信息确认发送" + COUNT + "条信息所耗时间：" + (end - start) + "ms");
    }

    //异步发布确认
    public static void publicMessageAsync() throws Exception {
        String queueName = UUID.randomUUID().toString();
        Channel channel = getConfirmChannel(queueName);


//        ConcurrentHashMap<Long, String> outstandingConfirms = new ConcurrentHashMap<>();

        long start = System.currentTimeMillis();
        // 消息确认成功回调函数
        ConfirmCallback ackCallback = (deliveryTag, multiply) -> {
            System.out.println("确认的消息：" + deliveryTag);
        };
        // 消息确认失败回调函数
        /*
         * 参数1：消息的标记
         * 参数2：是否为批量确认
         * */
        ConfirmCallback nackCallback = (deliveryTag, multiply) -> {
            System.out.println("未确认的消息：" + deliveryTag);
        };
        // 准备消息的监听器，监听哪些消息成功，哪些消息失败
        /*
         * 参数1：监听哪些消息成功
         * 参数2：监听哪些消息失败
         * */
        channel.addConfirmListener(ackCallback, nackCallback);
        // 批量发送消息
        for (int i = 0; i < COUNT; i++) {
            String message = "消息" + i;
            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
        }

        long end = System.currentTimeMillis();
        System.out.println("异步信息确认发送" + COUNT + "条信息所耗时间：" + (end - start) + "ms");
    }
}
