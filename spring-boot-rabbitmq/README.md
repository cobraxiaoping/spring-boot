# springboot-rabbitmq

springboot 2.x+rabbitmq3.7.15

演示了三种模式，topic,fanout,direct

rabbitmq的安装参照：

`docker pull rabbitmq:3-management`

启动rabbitmq

`docker run  --name some-rabbitmq -d -p 15672:15672  -p 5672:5672 -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin rabbitmq:3-management`

启动以后 rabbitmq的访问地址为：http://ip:15672/



