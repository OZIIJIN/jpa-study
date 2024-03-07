package me.springstudy.jpastudy.channel;

import static org.junit.jupiter.api.Assertions.*;

import me.springstudy.jpastudy.thread.ThreadRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(value = false)
class ChannelRepositoryTest {

	@Autowired
	private ChannelRepository channelRepository;

	@Test
	void 삽입_조회_성공() {
		// given
		Channel newChannel = Channel.builder().name("newGrooup").build();

		// when
		Channel savedChannel = channelRepository.insertChannel(newChannel);

		// then
		Channel foundChannel = channelRepository.selectChannel(savedChannel.getId());
		assert foundChannel.getId().equals(savedChannel.getId());
	}
}