package me.springstudy.jpastudy.thread;

import static org.junit.jupiter.api.Assertions.*;

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
		Thread newThread = Thread.builder().message("new-message").build();
		newThread.setChannel(newChannel);

		// when
		Thread savedThread = threadRepository.insertThread(newThread);
		Channel savedChannel = channelRepository.insertChannel(newChannel);

		// then
		Thread foundThread = threadRepository.selectThread(savedThread.getId());
		assert foundThread.getId().equals(savedThread.getId());
	}

}