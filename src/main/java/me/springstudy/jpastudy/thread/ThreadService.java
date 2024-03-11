package me.springstudy.jpastudy.thread;

import java.util.List;
import me.springstudy.jpastudy.user.User;

public interface ThreadService {

	List<Thread> selectMentionedThreadList(User user);

	Thread insert(Thread thread);

}
