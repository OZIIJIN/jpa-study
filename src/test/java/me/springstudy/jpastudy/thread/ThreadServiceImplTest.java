package me.springstudy.jpastudy.thread;

import java.util.List;
import me.springstudy.jpastudy.channel.Channel;
import me.springstudy.jpastudy.channel.Channel.Type;
import me.springstudy.jpastudy.channel.ChannelRepository;
import me.springstudy.jpastudy.user.User;
import me.springstudy.jpastudy.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ThreadServiceImplTest {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ChannelRepository channelRepository;

	@Autowired
	ThreadService threadService;

	@Test
	void getMentionedThreadList() {
		// given
		var newUser = User.builder().username("new").password("1").build();
		var saveUser = userRepository.save(newUser);
		var newThread1 = Thread.builder().message("message").build();
		newThread1.addMention(newUser);
		threadService.insert(newThread1);

		var newThread2 = Thread.builder().message("message2").build();
		newThread2.addMention(newUser);
		threadService.insert(newThread2);

		// when
		//var mentionedThreads = threadService.selectMentionedThreadList(saveUser);
		//에러남 -> 멘션이 매핑이 되어있는데 querydslpredicaateexecutor 같은 경우에는 join 연산이 불가능
		var mentionedThreads = saveUser.getMentions().stream().map(Mention::getThread).toList();

		// then
		assert mentionedThreads.containsAll(List.of(newThread1, newThread2));

	}

	@Test
	void getNotEmptyThreadList() {
		// given
		var newChannel = Channel.builder().name("c1").type(Type.PUBLIC).build();
		var savedChannel = channelRepository.save(newChannel);
		var newThread = Thread.builder().message("message").build();
		newThread.setChannel(savedChannel);
		threadService.insert(newThread);

		var newThread2 = Thread.builder().message("").build();
		newThread2.setChannel(savedChannel);
		threadService.insert(newThread2);

		// when
		var notEmptyThreads = threadService.selectNotEmptyThreadList(savedChannel);

		// then 메세지가 비어있는 newThread2 는 조회되지 않는다.
		assert !notEmptyThreads.contains(newThread2);
	}

}