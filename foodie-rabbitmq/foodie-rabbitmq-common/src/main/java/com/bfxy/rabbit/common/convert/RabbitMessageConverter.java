package com.bfxy.rabbit.common.convert;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import com.google.common.base.Preconditions;

public class RabbitMessageConverter implements MessageConverter {

    private GenericMessageConverter delegate;

    public RabbitMessageConverter(GenericMessageConverter genericMessageConverter) {
        Preconditions.checkNotNull(genericMessageConverter);
        this.delegate = genericMessageConverter;
    }

    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        com.bfxy.rabbit.api.Message message = (com.bfxy.rabbit.api.Message)object;
        messageProperties.setDelay(message.getDelayMills());
        return this.delegate.toMessage(object, messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        com.bfxy.rabbit.api.Message msg = (com.bfxy.rabbit.api.Message) this.delegate.fromMessage(message);
        return msg;
    }

}