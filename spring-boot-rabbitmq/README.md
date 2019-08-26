# springboot-rabbitmq

springboot 2.x+rabbitmq3.7.15

演示了三种模式，topic,fanout,direct

rabbitmq的安装参照：

`docker pull rabbitmq:3-management`

启动rabbitmq

`docker run  --name some-rabbitmq -d -p 15672:15672  -p 5672:5672 -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin rabbitmq:3-management`

启动以后 rabbitmq的访问地址为：http://ip:15672/



## rabbitmq三种模式简介

RabbitMQ提供了四种Exchange：fanout,direct,topic,header，header模式在实际使用中较少，

本文只对前三种模式进行比较。

性能排序：fanout > direct >> topic。比例大约为11：10：6



#### Fanout Exchange

Fanout Exchange – 不处理路由键。你只需要简单的将队列绑定到交换机上。一个发送到交换机的消息都会被转发到与该交换机绑定的所有队列上。很像子网广播，每台子网内的主机都获得了一份复制的消息。Fanout交换机转发消息是最快的。 

任何发送到Fanout Exchange的消息都会被转发到与该Exchange绑定(Binding)的所有Queue上。

1.可以理解为路由表的模式

2.这种模式不需要RouteKey

3.这种模式需要提前将Exchange与Queue进行绑定，一个Exchange可以绑定多个Queue，一个Queue可以同多个Exchange进行绑定。

4.如果接受到消息的Exchange没有与任何Queue绑定，则消息会被抛弃。



#### Direct Exchange

Direct Exchange - 处理路由键。需要将一个队列绑定到交换机上，要求该消息与一个特定的路由键完全匹配。这是一个完整的匹配。如果一个队列绑定到该交换机上要求路由键 “dog”，则只有被标记为“dog”的消息才被转发，不会转发dog.puppy，也不会转发dog.guard，只会转发dog。 

任何发送到Direct Exchange的消息都会被转发到RouteKey中指定的Queue。

1.一般情况可以使用rabbitMQ自带的Exchange：”"(该Exchange的名字为空字符串，下文称其为default Exchange)。

2.这种模式下不需要将Exchange进行任何绑定(binding)操作

3.消息传递时需要一个“RouteKey”，可以简单的理解为要发送到的队列名字。

4.如果vhost中不存在RouteKey中指定的队列名，则该消息会被抛弃。



#### Topic Exchange 

Topic Exchange – 将路由键和某模式进行匹配。此时队列需要绑定要一个模式上。符号“#”匹配一个或多个词，符号“*”匹配不多不少一个词。因此“audit.#”能够匹配到“audit.irs.corporate”，但是“audit.*” 只会匹配到“audit.irs”

任何发送到Topic Exchange的消息都会被转发到所有关心RouteKey中指定话题的Queue上

1.这种模式较为复杂，简单来说，就是每个队列都有其关心的主题，所有的消息都带有一个“标题”(RouteKey)，Exchange会将消息转发到所有关注主题能与

RouteKey模糊匹配的队列。

2.这种模式需要RouteKey，也许要提前绑定Exchange与Queue。

3.在进行绑定时，要提供一个该队列关心的主题，如“#.log.#”表示该队列关心所有涉及log的消息(一个RouteKey为”MQ.log.error”的消息会被转发到该队列)。

4.“#”表示0个或若干个关键字，“*”表示一个关键字。如“log.*”能与“log.warn”匹配，无法与“log.warn.timeout”匹配；但是“log.#”能与上述两者匹配。

5.同样，如果Exchange没有发现能够与RouteKey匹配的Queue，则会抛弃此消息。



## rabbit 手动确认消息

配置消息确认方式，其有三种配置方式，分别是none、manual和auto；默认auto

spring.rabbitmq.listener.simple.acknowledge-mode=manual

启用消息发布确认

spring.rabbitmq.publisher-confirms=true

启用发布返回

spring.rabbitmq.publisher-returns=true



实现消息确认回调，以及消息返回

```java
package com.web.cobra.xp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;

public class RabbitmqCallbackImpl implements ConfirmCallback, ReturnCallback {

	private Logger log = LoggerFactory.getLogger(RabbitmqCallbackImpl.class);

	/*
	 * (消息发送到交换机确认机制，通过实现ConfirmCallback接口， 消息发送到Broker后触发回调，确认消息是否到达Borker服务器，
	 * 也就是只确认是否正确到达Exchange中。)
	 */
	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		log.info("关联数据：{},ack:{},cause:{}", correlationData, ack, cause);
		if (ack) {
			log.info("消息到达rabbitmq服务器");
		} else {
			log.info("消息可能未到达rabbitmq服务器");
		}
	}

	/*
	 * (通过实现ReturnCallBack接口，启动消息失败返回，比如路由不到队列时触发回调。)
	 */
	@Override
	public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
		String correlationId = message.getMessageProperties().getCorrelationId();
		log.debug("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}", correlationId, replyCode, replyText, exchange,
				routingKey);
	}

}

```



在配置rabbitmqTemplate

```java
	@Autowired
	private RabbitTemplate rabbitTemplate;
 
	@PostConstruct
	public void initRabbitTemplate() {
		// 设置生产者消息确认
		rabbitTemplate.setConfirmCallback(new RabbitmqCallbackImpl());
	}
```



手动确认消息参照

```java
	@RabbitListener(queues = RabbitmqConfig.DIRECT_QUEUE_FIRST)
	public void receiveDirectQueueFirst(Message message,Channel channel,User user) {
		System.out.println("【receiveDirectQueueFirst监听到消息】" + user.toString());
		try {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
```



## 死信队列设计

### 死信队列简介

在大多数的MQ中间件中，都有死信队列的概念。死信队列同其他的队列一样都是普通的队列。在RabbitMQ中并没有特定的“死信队列”类型，而是通过配置，将其实现。
当我们在创建一个业务的交换机和队列的时候，可以配置参数，指明另一个队列为当前队列的死信队列，在RabbitMQ中，死信队列（严格的说应该是死信交换机）被称为DLX Exchange。当消息“死掉”后，会被自动路由到DLX Exchange的queue中。

### 什么样的消息会进入死信队列？

1.消息的TTL过期。

2.消费者对broker应答Nack，并且消息禁止重回队列。(spring.rabbitmq.listener.simple.default-requeue-rejected:false)

3.Queue队列长度已达上限。

### 场景分析

以用户订单支付为场景。在各大电商平台上，订单的都有待支付时间，通常为30min。当用户超过30min未支付订单，该订单的状态应该会变成“超时取消”，或类似的状态值的改变。
如果不使用MQ，可以设计一个定时任务，定时查询数据库，判断订单的状态和支付时间是否已经到期，若到期则修改订单的状态。但显然，这不是一个很好的操作，频繁访问数据库，造成不必要的资源浪费。
使用MQ，我们可以在下单的时候，当订单数据入库后，发送一条Message到Queue中，并设置过期时间为30min或自定义的支付过期时间。



死信队列的使用参照：<https://blog.csdn.net/shishishi777/article/details/99879419>











## spring-boot整合rabbitmq参数详解

\# base
spring.rabbitmq.host: 服务Host

spring.rabbitmq.port: 服务端口

spring.rabbitmq.username: 登陆用户名

spring.rabbitmq.password: 登陆密码

spring.rabbitmq.virtual-host: 连接到rabbitMQ的vhost

spring.rabbitmq.addresses: 指定client连接到的server的地址，多个以逗号分隔(优先取addresses然后再取host)

spring.rabbitmq.requested-heartbeat: 指定心跳超时，单位秒，0为不指定；默认60s

spring.rabbitmq.publisher-confirms: 是否启用【发布确认】

spring.rabbitmq.publisher-returns: 是否启用【发布返回】

spring.rabbitmq.connection-timeout: 连接超时，单位毫秒，0表示无穷大，不超时

spring.rabbitmq.parsed-addresses:

\# ssl
spring.rabbitmq.ssl.enabled: 是否支持ssl

spring.rabbitmq.ssl.key-store: 指定持有SSL certificate的key store的路径

spring.rabbitmq.ssl.key-store-password: 指定访问key store的密码

spring.rabbitmq.ssl.trust-store: 指定持有SSL certificates的Trust store

spring.rabbitmq.ssl.trust-store-password: 指定访问trust store的密码

spring.rabbitmq.ssl.algorithm: ssl使用的算法，例如，TLSv1.1



\# cache
spring.rabbitmq.cache.channel.size: 缓存中保持的channel数量

spring.rabbitmq.cache.channel.checkout-timeout: 当缓存数量被设置时，从缓存中获取一个channel的超时时间，单位毫秒；如果为0，则总是创建一个新channel

spring.rabbitmq.cache.connection.size: 缓存的连接数，只有是CONNECTION模式时生效

spring.rabbitmq.cache.connection.mode: 连接工厂缓存模式：CHANNEL 和 CONNECTION



\# listener
spring.rabbitmq.listener.simple.auto-startup: 是否启动时自动启动容器

spring.rabbitmq.listener.simple.acknowledge-mode: 表示消息确认方式，其有三种配置方式，分别是none、manual和auto；默认auto

spring.rabbitmq.listener.simple.concurrency: 最小的消费者数量

spring.rabbitmq.listener.simple.max-concurrency: 最大的消费者数量

spring.rabbitmq.listener.simple.prefetch: 指定一个请求能处理多少个消息，如果有事务的话，必须大于等于transaction数量.

spring.rabbitmq.listener.simple.transaction-size: 指定一个事务处理的消息数量，最好是小于等于prefetch的数量.

spring.rabbitmq.listener.simple.default-requeue-rejected: 决定被拒绝的消息是否重新入队；默认是true（与参数acknowledge-mode有关系）

spring.rabbitmq.listener.simple.idle-event-interval: 多少长时间发布空闲容器时间，单位毫秒

spring.rabbitmq.listener.simple.retry.enabled: 监听重试是否可用

spring.rabbitmq.listener.simple.retry.max-attempts: 最大重试次数

spring.rabbitmq.listener.simple.retry.initial-interval: 第一次和第二次尝试发布或传递消息之间的间隔

spring.rabbitmq.listener.simple.retry.multiplier: 应用于上一重试间隔的乘数

spring.rabbitmq.listener.simple.retry.max-interval: 最大重试时间间隔

spring.rabbitmq.listener.simple.retry.stateless: 重试是有状态or无状态



\# template
spring.rabbitmq.template.mandatory: 启用强制信息；默认false

spring.rabbitmq.template.receive-timeout: receive() 操作的超时时间

spring.rabbitmq.template.reply-timeout: sendAndReceive() 操作的超时时间

spring.rabbitmq.template.retry.enabled: 发送重试是否可用 

spring.rabbitmq.template.retry.max-attempts: 最大重试次数

spring.rabbitmq.template.retry.initial-interval: 第一次和第二次尝试发布或传递消息之间的间隔

spring.rabbitmq.template.retry.multiplier: 应用于上一重试间隔的乘数

spring.rabbitmq.template.retry.max-interval: 最大重试时间间隔



属性配置可参照：<https://blog.csdn.net/en_joker/article/details/80103519>

