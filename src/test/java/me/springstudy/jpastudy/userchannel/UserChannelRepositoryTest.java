package me.springstudy.jpastudy.userchannel;

import me.springstudy.jpastudy.channel.Channel;
import me.springstudy.jpastudy.channel.ChannelRepository;
import me.springstudy.jpastudy.user.User;
import me.springstudy.jpastudy.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(value = false)
class UserChannelRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ChannelRepository channelRepository;

	@Autowired
	private UserChannelRepository userChannelRepository;

	@Test
	void userJoinChannelTest() {
		// given
		Channel newChannel = Channel.builder().name("new-channel").build();
		User newUser = User.builder().username("new-user").password("new-pass").build();
		UserChannel newUserChannel = newChannel.joinUser(newUser);

		// when
		Channel savedChannel = channelRepository.insertChannel(newChannel);
		User savedUser = userRepository.insertUser(newUser);
		UserChannel savedUserChannel = userChannelRepository.insertUserChannel(newUserChannel);

		// then
		Channel foundChaneel = channelRepository.selectChannel(savedChannel.getId());
		assert foundChaneel.getUserChannels().stream()
			.map(UserChannel::getChannel)
			.map(Channel::getName)
			.anyMatch(name -> name.equals(newChannel.getName()));
	}

	@Test
	void userJoinChannelWithCascadeTest() {
		// given
		Channel newChannel = Channel.builder().name("new-channel").build();
		User newUser = User.builder().username("new-user").password("new-pass").build();
		newChannel.joinUser(newUser);

		// when
		Channel savedChannel = channelRepository.insertChannel(newChannel);
		User savedUser = userRepository.insertUser(newUser);

		// then
		Channel foundChaneel = channelRepository.selectChannel(savedChannel.getId());
		assert foundChaneel.getUserChannels().stream()
			.map(UserChannel::getChannel)
			.map(Channel::getName)
			.anyMatch(name -> name.equals(newChannel.getName()));
	}
}