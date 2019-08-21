package com.web.cobra.xp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.web.cobra.xp.entity.User;
import com.web.cobra.xp.sender.DirectSender;
import com.web.cobra.xp.sender.FanoutSender;
import com.web.cobra.xp.sender.TopicSender;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootRabbitmqApplicationTests {

	@Autowired
	private FanoutSender fanoutSender;

	@Autowired
	private TopicSender topicSender;

	@Autowired
	private DirectSender directSender;

	@Test
	public void testTopic() {
		User user = createUser();
		topicSender.send(user);
	}

	@Test
	public void testFanout() {
		User user = createUser();
		fanoutSender.send(user);
	}

	@Test
	public void testDirect() {
		User user = createUser();
		directSender.send(user);
	}

	public User createUser() {
		User user = new User();
		user.setId("1");
		user.setName("cobrayang");
		return user;
	}
}
