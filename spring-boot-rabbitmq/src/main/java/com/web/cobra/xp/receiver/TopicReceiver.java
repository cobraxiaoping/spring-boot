package com.web.cobra.xp.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.web.cobra.xp.config.RabbitmqConfig;
import com.web.cobra.xp.entity.User;

@Component
public class TopicReceiver {
	// queues是指要监听的队列的名字
	@RabbitListener(queues = RabbitmqConfig.TOPIC_QUEUE_FIRST)
	public void receiveTopicFirst(User user) {
		System.out.println("【receiveTopicFirst监听到消息】" + user.toString());
	}

	@RabbitListener(queues = RabbitmqConfig.TOPIC_QUEUE_SECOND)
	public void receiveTopicSecond(User user) {
		System.out.println("【receiveTopicSecond监听到消息】" + user.toString());
	}

}
