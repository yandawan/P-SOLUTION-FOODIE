package com.bfxy.rabbit.producer.broker;

import com.bfxy.rabbit.api.Message;

/**
 *	RabbitBroker 具体发送不同种类型消息的接口
 */
public interface RabbitBroker {

	/**
	 * 迅速消息
	 */
	void rapidSend(Message message);

	/**
	 * 确认消息
	 */
	void confirmSend(Message message);

	/**
	 * 可靠消息
	 */
	void reliantSend(Message message);

	/**
	 * 批量消息
	 */
	void sendMessages();
	
}
