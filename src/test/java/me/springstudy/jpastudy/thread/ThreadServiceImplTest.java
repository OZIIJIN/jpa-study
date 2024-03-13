package me.springstudy.jpastudy.thread;

import java.util.List;
import me.springstudy.jpastudy.channel.Channel;
import me.springstudy.jpastudy.channel.Channel.Type;
import me.springstudy.jpastudy.channel.ChannelRepository;
import me.springstudy.jpastudy.comment.Comment;
import me.springstudy.jpastudy.comment.CommentRepository;
import me.springstudy.jpastudy.common.PageDTO;
import me.springstudy.jpastudy.mention.ThreadMention;
import me.springstudy.jpastudy.user.User;
import me.springstudy.jpastudy.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
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

	@Autowired
	CommentRepository commentRepository;

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
		var mentionedThreads = saveUser.getThreadMentions().stream().map(ThreadMention::getThread)
			.toList();

		// then
		assert mentionedThreads.containsAll(List.of(newThread1, newThread2));

	}

	@Test
	void getNotEmptyThreadListTest() {
		// given
		var newChannel = Channel.builder().name("c1").type(Type.PUBLIC).build();
		var savedChannel = channelRepository.save(newChannel);
		getTestThread("message", savedChannel);

		var newThread2 = getTestThread("", savedChannel);

		// when
		var notEmptyThreads = threadService.selectNotEmptyThreadList(savedChannel);

		// then 메세지가 비어있는 newThread2 는 조회되지 않는다.
		assert !notEmptyThreads.contains(newThread2);
	}


	@Test
	@DisplayName("전체 채널에서 내가 멘션된 쓰레드 상세정보 목록 테스트")
	void selectMentionedThreadListTest() {
		// given
		var user = getTestUser("1", "1");
		var user2 = getTestUser("2", "2");
		var user3 = getTestUser("3", "3");
		var user4 = getTestUser("4", "4");
		var newChannel = Channel.builder().name("c1").type(Type.PUBLIC).build();
		var savedChannel = channelRepository.save(newChannel);
		var thread1 = getTestThread("message1", savedChannel, user, user2, "e1", user3, "comment",
			user4, "commentEmotion1");
		var thread2 = getTestThread("message2", savedChannel, user, user2, "e2", user3, "comment2",
			user4, "commentEmotion2");

		// when
		var mentionedThreadList = threadService.selectMentionedThreadList(user.getId(),
			PageDTO.builder().currentPage(1).size(100).build());

		// then
		assert mentionedThreadList.getTotalElements() == 2;

	}

	private User getTestUser(String username, String password) {
		var newUser = User.builder().username(username).password(password).build();
		return userRepository.save(newUser);
	}

	private Comment getTestComment(User user, String commentMessage) {
		Comment newComment = Comment.builder().messge(commentMessage).build();
		newComment.setUser(user);
		return commentRepository.save(newComment);
	}

	private Thread getTestThread(String message, Channel savedChannel) {
		var newThread = Thread.builder().message(message).build();
		newThread.setChannel(savedChannel);
		return threadService.insert(newThread);
	}

	private Thread getTestThread(String message, Channel channel, User mentionedUser) {
		var newThread = getTestThread(message, channel);
		newThread.addMention(mentionedUser);
		return threadService.insert(newThread);
	}

	private Thread getTestThread(String message, Channel channel, User mentionedUser,
		User emotionUser, String emotionValue) {
		var newThread = getTestThread(message, channel, mentionedUser);
		newThread.addEmotion(emotionUser, emotionValue);
		return threadService.insert(newThread);
	}

	private Thread getTestThread(String message, Channel channel, User mentionedUser,
		User emotionUser, String emotionValue, User commentUser, String commentMessage) {
		var newThread = getTestThread(message, channel, mentionedUser, emotionUser, emotionValue);
		newThread.addComment(getTestComment(commentUser, commentMessage));
		return threadService.insert(newThread);
	}

	private Thread getTestThread(String message, Channel channel, User mentionedUser,
		User emotionUser, String emotionValue, User commentUser, String commentMessage,
		User commentEmotionUser, String commentEmotionValue) {
		var newThread = getTestThread(message, channel, mentionedUser, emotionUser, emotionValue,
			commentUser, commentMessage);
		var newComment = getTestComment(commentUser, commentMessage);
		newThread.getComments()
			.forEach(comment -> comment.addEmotion(commentEmotionUser, commentEmotionValue));
		return threadService.insert(newThread);
	}

}