package com.web.cobra.xp.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.web.cobra.xp.config.RabbitmqConfig;
import com.web.cobra.xp.entity.MailMessage;
import com.web.cobra.xp.service.MailService;

@Component
public class DirectReceiver {

	@Autowired
	private MailService emailService;

	// queues是指要监听的队列的名字
	@RabbitListener(queues = RabbitmqConfig.DIRECT_MAIL_QUEUE)
	public void receiveDirectMailQueue(MailMessage email) {
		System.out.println("【receiveDirectMailQueue监听到消息】" + email.toString());
		if (!ObjectUtils.isEmpty(email.getTo())) {
			for (String to : email.getTo()) {
				emailService.sendSimpleMail(email.getFrom(),to, email.getSubject(), email.getContent());
			}
		}
	}
}
