package com.web.cobra.xp.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.web.cobra.xp.config.RabbitmqConfig;
import com.web.cobra.xp.entity.User;

@Component
public class DirectReceiver {
	// queues是指要监听的队列的名字
	@RabbitListener(queues = RabbitmqConfig.DIRECT_MAIL_QUEUE)
	public void receiveDirectQueueFirst(User user) {
		System.out.println("【receiveDirectQueueFirst监听到消息】" + user.toString());
	}

}
