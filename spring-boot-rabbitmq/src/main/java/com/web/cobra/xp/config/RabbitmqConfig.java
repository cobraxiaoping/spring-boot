package com.web.cobra.xp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;

@Configuration
public class RabbitmqConfig {
	// topic
	public static final String TOPIC_QUEUE_FIRST = "topic.queue.first";
	public static final String TOPIC_QUEUE_SECOND = "topic.queue.second";
	public static final String TOPIC_EXCHANGE = "topic.exchage";

	// fanout
	public static final String FANOUT_QUEUE_FIRST = "fanout.queue.first";
	public static final String FANOUT_QUEUE_SECOND = "fanout.queue.second";
	public static final String FANOUT_EXCHANGE = "fanout.exchage";

	// direct
	public static final String DIRECT_QUEUE_FIRST = "direct.queue.first";
	public static final String DIRECT_QUEUE_SECOND = "direct.queue.second";
	public static final String DIRECT_EXCHANGE = "direct.exchange";

	/**************** 创建队列开始 ***************/

	@Bean
	public Queue topicQueueFirst() {
		return new Queue(TOPIC_QUEUE_FIRST);
	}

	@Bean
	public Queue topicQueueSecond() {
		return new Queue(TOPIC_QUEUE_SECOND);
	}

	@Bean
	public Queue fanoutQueueFirst() {
		return new Queue(FANOUT_QUEUE_FIRST);
	}

	@Bean
	public Queue fanoutQueueSecond() {
		return new Queue(FANOUT_QUEUE_SECOND);
	}

	@Bean
	public Queue directQueueFirst() {
		return new Queue(DIRECT_QUEUE_FIRST);
	}

	@Bean
	public Queue directQueueSecond() {
		return new Queue(DIRECT_QUEUE_SECOND);
	}

	/**************** 创建队列结束 ***************/

	/**************** 创建交换机开始 ***************/

	@Bean
	public TopicExchange topicExchange() {
		return new TopicExchange(TOPIC_EXCHANGE);
	}

	@Bean
	public FanoutExchange fanoutExchange() {
		return new FanoutExchange(FANOUT_EXCHANGE);
	}

	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange(DIRECT_EXCHANGE);
	}

	/**************** 创建交换机结束 ***************/

	/**************** 队列和交换机绑定开始 ***************/

	@Bean
	public Binding topicBinding1() {
		return BindingBuilder.bind(topicQueueFirst()).to(topicExchange()).with("cobra.message");
	}

	@Bean
	public Binding topicBinding2() {
		return BindingBuilder.bind(topicQueueSecond()).to(topicExchange()).with("cobra.#");
	}

	@Bean
	public Binding fanoutQueueFirstBinding() {
		return BindingBuilder.bind(fanoutQueueFirst()).to(fanoutExchange());
	}

	@Bean
	public Binding fanoutQueueSecondBinding() {
		return BindingBuilder.bind(fanoutQueueSecond()).to(fanoutExchange());
	}
	
	
	@Bean
    public Binding directQueueFirstBinding() {
        return BindingBuilder.bind(directQueueFirst()).to(directExchange()).with("direct.pwl");
    }
	
	@Bean
    public Binding directQueueSecondBinding() {
        return BindingBuilder.bind(directQueueSecond()).to(directExchange()).with("direct.pwl1");
    }

	/**************** 交换机和队列绑定结束 ***************/

}
