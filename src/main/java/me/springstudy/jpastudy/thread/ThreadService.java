package me.springstudy.jpastudy.thread;

import java.util.List;
import me.springstudy.jpastudy.channel.Channel;
import me.springstudy.jpastudy.common.PageDTO;
import org.springframework.data.domain.Page;

public interface ThreadService {

	List<Thread> selectNotEmptyThreadList(Channel channel);

	Page<Thread> selectMentionedThreadList(Long userId, PageDTO pageDTO);

	Thread insert(Thread thread);

}
