package com.web.cobra.xp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitmqConfig {

	// direct
	public static final String DIRECT_MAIL_QUEUE = "mail.queue";
	public static final String DIRECT_EXCHANGE = "direct.exchange";

	/**************** 创建队列开始 **********************/
	@Bean
	public Queue directQueueMail() {
		return new Queue(DIRECT_MAIL_QUEUE);
	}
	/**************** 创建队列结束 **********************/
	

	/**************** 创建交换机开始 ********************/
	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange(DIRECT_EXCHANGE);
	}
	/**************** 创建交换机结束********************/
	

	/**************** 队列和交换机绑定开始 ***************/
	@Bean
	public Binding directQueueFirstBinding() {
		return BindingBuilder.bind(directQueueMail()).to(directExchange()).with("eamil");
	}
	/**************** 交换机和队列绑定结束 ***************/

}
