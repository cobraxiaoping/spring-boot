package com.web.cobra.xp.sender;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.web.cobra.xp.config.RabbitmqConfig;
import com.web.cobra.xp.entity.User;

@Component
public class DirectSender {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void send(User user) {
		this.rabbitTemplate.convertAndSend(RabbitmqConfig.DIRECT_EXCHANGE, "direct.pwl", user);
	}

}
