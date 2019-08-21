package com.web.cobra.xp.sender;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.web.cobra.xp.config.RabbitmqConfig;
import com.web.cobra.xp.entity.User;

@Component
public class TopicSender {
	@Autowired
	private AmqpTemplate rabbitTemplate;

	// 第一个参数：TopicExchange名字
	// 第二个参数：Route-Key
	// 第三个参数：要发送的内容
	public void send(User user) {
		this.rabbitTemplate.convertAndSend(RabbitmqConfig.TOPIC_EXCHANGE, "cobra.message", user);
		this.rabbitTemplate.convertAndSend(RabbitmqConfig.TOPIC_EXCHANGE, "cobra.xp", user);
	}

}
