springboot2.x整合redis

配置参数说明

| 配置                                 | 说明                                         |
| ------------------------------------ | -------------------------------------------- |
| spring.redis.database=0              | redis数据库索引默认为0                       |
| spring.redis.host=127.0.0.1          | redis服务器地址                              |
| spring.redis.port=6379               | redis服务器端口                              |
| spring.redis.password=               | redis服务器连接密码，默认为空                |
| spring.redis.jedis.pool.max-active=8 | 连接池最大连接数，使用负值表示没有限制       |
| spring.redis.jedis.pool.max-wait=-1  | 连接池最大阻塞等待时间，使用负值表示没有限制 |
| spring.redis.jedis.pool.max-idle=8   | 连接池中的最大空闲连接                       |
| spring.redis.jedis.pool.min-idle=0   | 连接池中的最小空闲连接                       |
| spring.redis.timeout=0               | 链接超时时间单位毫秒                         |



在SpringBoot2.0之后，spring容器是自动的生成了StringRedisTemplate和RedisTemplate<Object,Object>，可以直接注入并使用

但是在实际使用中，我们大多不会直接使用RedisTemplate<Object,Object>，而是会对key,value进行序列化，所以我们还需要新增一个配置类自定义的RedisTemplate。



注意：

```
stringRedisTemplate是操作字符串
redisTemplate 操作对象
```





运行redis

```shell
#拉取镜像
docker pull redis:5.0.5
#启动redis
docker run  -itd --name redis -p 6379:6379 redis:5.0.5

```

