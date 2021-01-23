package com.bfxy.rabbit.producer.broker;

import com.bfxy.rabbit.api.Message;
import com.bfxy.rabbit.api.MessageType;
import com.bfxy.rabbit.api.exception.MessageRunTimeException;
import com.bfxy.rabbit.common.convert.GenericMessageConverter;
import com.bfxy.rabbit.common.convert.RabbitMessageConverter;
import com.bfxy.rabbit.common.serializer.Serializer;
import com.bfxy.rabbit.common.serializer.SerializerFactory;
import com.bfxy.rabbit.common.serializer.impl.JacksonSerializerFactory;
import com.bfxy.rabbit.producer.service.MessageStoreService;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 	RabbitTemplateContainer池化封装: 每一个topic 对应一个RabbitTemplate
 *	1 提高发送的效率
 * 	2 可以根据不同的需求制定化不同的RabbitTemplate, 比如每一个topic 都有自己的routingKey规则
 */
@Slf4j
@Component
public class RabbitTemplateContainer implements RabbitTemplate.ConfirmCallback {

	private Map<String, RabbitTemplate> rabbitMap = Maps.newConcurrentMap();
	
	private Splitter splitter = Splitter.on("#");
	
	private SerializerFactory serializerFactory = JacksonSerializerFactory.INSTANCE;
	
	@Autowired
	private ConnectionFactory connectionFactory;
	
	@Autowired
	private MessageStoreService messageStoreService;
	
	public RabbitTemplate getTemplate(Message message) throws MessageRunTimeException {
		Preconditions.checkNotNull(message);
		String topic = message.getTopic();
		RabbitTemplate rabbitTemplate = rabbitMap.get(topic);
		if(rabbitTemplate != null) {
			return rabbitTemplate;
		}
		log.info("#RabbitTemplateContainer.getTemplate# topic: {} is not exists, create one", topic);
		
		RabbitTemplate newTemplate = new RabbitTemplate(connectionFactory);
		newTemplate.setExchange(topic);
		newTemplate.setRoutingKey(message.getRoutingKey());
		newTemplate.setRetryTemplate(new RetryTemplate());
		
		// 添加序列化反序列化和converter对象
		Serializer serializer = serializerFactory.create();
		GenericMessageConverter gmc = new GenericMessageConverter(serializer);
		RabbitMessageConverter rmc = new RabbitMessageConverter(gmc);
		newTemplate.setMessageConverter(rmc);
		
		String messageType = message.getMessageType();
		// 只要不是迅速消息 其他的都需要应答
		if(!MessageType.RAPID.equals(messageType)) {
			newTemplate.setConfirmCallback(this);
		}
		// 如果存在就不向里面put
		rabbitMap.putIfAbsent(topic, newTemplate);
		
		return rabbitMap.get(topic);
	}

	/**
	 *	无论是 Confirm 消息,还是 Reliant 消息,发送消息以后 Broker 都会去回调 Confirm
	 */
	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		//	具体的消息应答
		List<String> strings = splitter.splitToList(correlationData.getId());
		String messageId = strings.get(0);
		long sendTime = Long.parseLong(strings.get(1));
		String messageType = strings.get(2);
		if(ack) {
			//	当Broker 返回ACK成功时, 就是更新一下日志表里对应的消息发送状态为 SEND_OK
			//	如果当前消息类型为reliant 我们就去数据库查找并进行更新
			if(MessageType.RELIANT.endsWith(messageType)) {
				this.messageStoreService.succuess(messageId);
			}
			log.info("send message is OK, confirm messageId: {}, sendTime: {}", messageId, sendTime);
		} else {
			log.error("send message is Fail, confirm messageId: {}, sendTime: {}", messageId, sendTime);
		}
	}
}
