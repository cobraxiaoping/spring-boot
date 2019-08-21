package com.web.cobra.xp.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.web.cobra.xp.config.RabbitmqConfig;
import com.web.cobra.xp.entity.User;

@Component
public class FanoutReceiver {

	Logger log = LoggerFactory.getLogger(FanoutReceiver.class);

	@RabbitListener(queues = RabbitmqConfig.FANOUT_QUEUE_FIRST)
	public void receiveFanoutFirst(User user) {
		System.out.println("收到消息"+user.toString());
		log.info("【FANOUT_QUEUE_FIRST监听到消息】" + user);
	}
	
	@RabbitListener(queues = RabbitmqConfig.FANOUT_QUEUE_SECOND)
	public void receiveFanoutSecond(User user) {
		System.out.println("收到消息："+user.toString());
		log.info("【FANOUT_QUEUE_SECOND监听到消息】" + user);
	}
}
