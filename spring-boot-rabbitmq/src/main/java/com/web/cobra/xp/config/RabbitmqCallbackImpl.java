package com.web.cobra.xp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;

public class RabbitmqCallbackImpl implements ConfirmCallback, ReturnCallback {

	private Logger log = LoggerFactory.getLogger(RabbitmqCallbackImpl.class);

	/*
	 * (消息发送到交换机确认机制，通过实现ConfirmCallback接口， 消息发送到Broker后触发回调，确认消息是否到达Borker服务器，
	 * 也就是只确认是否正确到达Exchange中。)
	 */
	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		log.info("关联数据：{},ack:{},cause:{}", correlationData, ack, cause);
		if (ack) {
			log.info("消息到达rabbitmq服务器");
		} else {
			log.info("消息可能未到达rabbitmq服务器");
		}
	}

	/*
	 * (通过实现ReturnCallBack接口，启动消息失败返回，比如路由不到队列时触发回调。)
	 */
	@Override
	public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
		String correlationId = message.getMessageProperties().getCorrelationId();
		log.debug("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}", correlationId, replyCode, replyText, exchange,
				routingKey);
	}

}
