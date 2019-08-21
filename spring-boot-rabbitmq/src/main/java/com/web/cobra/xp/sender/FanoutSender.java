package com.web.cobra.xp.sender;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.web.cobra.xp.config.RabbitmqConfig;
import com.web.cobra.xp.entity.User;

@Component
public class FanoutSender {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	public void send(User user) {
		rabbitTemplate.convertAndSend(RabbitmqConfig.FANOUT_EXCHANGE, "", user);
	}
}
