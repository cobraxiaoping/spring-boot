package com.web.cobra.xp.receiver;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.web.cobra.xp.config.RabbitmqConfig;
import com.web.cobra.xp.entity.User;

@Component
public class DirectReceiver {
	// queues是指要监听的队列的名字
	@RabbitListener(queues = RabbitmqConfig.DIRECT_QUEUE_FIRST)
	public void receiveDirectQueueFirst(Message message,Channel channel,User user) {
		System.out.println("【receiveDirectQueueFirst监听到消息】" + user.toString());
		try {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RabbitListener(queues = RabbitmqConfig.DIRECT_QUEUE_SECOND)
	public void receiveDirectQueueSecond(User user) {
		System.out.println("【receiveDirectQueueSecond监听到消息】" + user.toString());
	}

}
