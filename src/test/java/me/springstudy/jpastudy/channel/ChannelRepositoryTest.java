package me.springstudy.jpastudy.channel;

import com.querydsl.core.types.Predicate;
import java.util.Optional;
import me.springstudy.jpastudy.user.User;
import me.springstudy.jpastudy.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(value = false)
@ActiveProfiles("test")
class ChannelRepositoryTest {

	@Autowired
	private ChannelRepository channelRepository;

	@Autowired
	private UserRepository userRepository;


	@Test
	void 삽입_조회_성공() {
		// given
		Channel newChannel = Channel.builder().name("newChannel").build();

		// when
		Channel savedChannel = channelRepository.save(newChannel);

		// then
		var foundChannel = channelRepository.findById(savedChannel.getId());
		assert foundChannel.get().getId().equals(savedChannel.getId());
	}

	@Test
	void queryDslTest() {
		// given
		var newChannel = Channel.builder().name("yejin").build();
		channelRepository.save(newChannel);

		Predicate predicate = QChannel.channel
			.name.equalsIgnoreCase("YEJIN");

		// when
		Optional<Channel> optional = channelRepository.findOne(predicate);

		// then
		assert optional.get().getName().equals(newChannel.getName());
	}

	@Test
	void contextHolderLifeCycleTest() {
		// given
		User newUser = User.builder().username("yejin").password("pass").build();
		User savedUser = userRepository.save(newUser);

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication authentication = new UsernamePasswordAuthenticationToken(
			savedUser,
			null);
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);

		Channel newChannel = Channel.builder().name("newChannel").build();
		Channel newChannel2 = Channel.builder().name("modifiedChannel").build();
		Channel savedChannel = channelRepository.save(newChannel);

		// when
		savedChannel.updateChannel(newChannel2);
		// then

	}

}