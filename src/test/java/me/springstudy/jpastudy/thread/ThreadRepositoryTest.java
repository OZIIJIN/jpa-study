package me.springstudy.jpastudy.thread;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import me.springstudy.jpastudy.channel.Channel;
import me.springstudy.jpastudy.channel.ChannelRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(value = false)
class ThreadRepositoryTest {

	@Autowired
	private ThreadRepository threadRepository;

	@Autowired
	private ChannelRepository channelRepository;

	@Test
	void Thread_삽입_조회_성공() {
		// given
		Channel newChannel = Channel.builder().name("new-channel").build();
		Thread newThread1 = Thread.builder().message("new-message").build();
		Thread newThread2 = Thread.builder().message("new-message1").build();
		newThread1.setChannel(newChannel);
		newThread2.setChannel(newChannel);

		// when
		Thread savedThread1 = threadRepository.insertThread(newThread1);
		Thread savedThread2 = threadRepository.insertThread(newThread2);
		Channel savedChannel = channelRepository.insertChannel(newChannel);

		// then
		Channel foundChannel = channelRepository.selectChannel(savedChannel.getId());
		assert foundChannel.getThreads().containsAll(Set.of(savedThread1, savedThread2));
	}

}