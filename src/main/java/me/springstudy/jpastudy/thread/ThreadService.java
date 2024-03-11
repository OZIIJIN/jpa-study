package me.springstudy.jpastudy.thread;

import java.util.List;
import me.springstudy.jpastudy.channel.Channel;

public interface ThreadService {

	List<Thread> selectNotEmptyThreadList(Channel channel);

	Thread insert(Thread thread);

}
