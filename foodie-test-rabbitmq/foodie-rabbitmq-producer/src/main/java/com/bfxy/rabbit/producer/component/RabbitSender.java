package com.bfxy.rabbit.producer.component;


import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 确认消息的回调监听接口，用于确认消息是否被broker所收到
     */
    final ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        /**
         * @param correlationData 作为一个唯一的标识
         * @param ack             broker 是否落盘成功
         * @param cause           失败的一些异常信息
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.err.println("消息ACK结果:" + ack + ", correlationData: " + correlationData.getId());
        }
    };

    /**
     * 对外发送消息的方法
     *
     * @param messageObject 具体的消息内容
     * @param properties    额外的附加属性
     */
    public void send(Object messageObject, Map<String, Object> properties) throws Exception {
        MessageHeaders messageHeaders = new MessageHeaders(properties);
        Message<?> message = MessageBuilder.createMessage(messageObject, messageHeaders);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        //  指定业务唯一的iD
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public org.springframework.amqp.core.Message postProcessMessage(org.springframework.amqp.core.Message message) throws AmqpException {
                System.err.println("---> post to do: " + message);
                return message;
            }
        };
        rabbitTemplate.convertAndSend("exchange-1", "springboot.rabbit", message, messagePostProcessor, correlationData);
    }
}
