package me.springstudy.jpastudy.userchannel;

import java.util.List;
import me.springstudy.jpastudy.channel.Channel;
import me.springstudy.jpastudy.channel.ChannelRepository;
import me.springstudy.jpastudy.common.PageDTO;
import me.springstudy.jpastudy.user.User;
import me.springstudy.jpastudy.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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

	@Test
	void userJoinChannelTest() {
		// given
		Channel newChannel = Channel.builder().name("new-channel").build();
		User newUser = User.builder().username("new-user").password("new-pass").build();
		UserChannel newUserChannel = newChannel.joinUser(newUser);

		// when
		Channel savedChannel = channelRepository.insertChannel(newChannel);
		User savedUser = userRepository.save(newUser);

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
		User savedUser = userRepository.save(newUser);

		// then
		Channel foundChaneel = channelRepository.selectChannel(savedChannel.getId());
		assert foundChaneel.getUserChannels().stream()
			.map(UserChannel::getChannel)
			.map(Channel::getName)
			.anyMatch(name -> name.equals(newChannel.getName()));
	}

	@Test
	void userCustomFieldSortingTest() {
		//given
		User newUser1 = User.builder().username("newUser").password("pass1").build();
		User newUser2 = User.builder().username("newUser").password("pass2").build();
		userRepository.save(newUser1);
		userRepository.save(newUser2);

		//when
		List<User> users = userRepository.findByUsername("newUser", Sort.by("customField"));

		//then
		assert users.get(0).getPassword().equals(newUser1.getPassword());

		//when
		users = userRepository.findByUsername("newUser", Sort.by("customField").descending());

		//then
		assert users.get(0).getPassword().equals(newUser2.getPassword());
	}

	@Test
	void pageDTOTest() {
		// given
		User newUser1 = User.builder().username("newUser").password("pass1").build();
		User newUser2 = User.builder().username("newUser").password("pass2").build();
		User newUser3 = User.builder().username("newUser").password("pass2").build();
		userRepository.save(newUser1);
		userRepository.save(newUser2);
		userRepository.save(newUser3);
		PageDTO pageDTO = new PageDTO(1, 2, "password");

		// when
		Page<User> page = userRepository.findAll(pageDTO.toPageable());

		// then
		assert page.getContent().size() == 2;
	}
}