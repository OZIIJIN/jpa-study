package me.springstudy.jpastudy.thread;

import com.mysema.commons.lang.IteratorAdapter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.springstudy.jpastudy.channel.Channel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ThreadServiceImpl implements ThreadService {

	private final ThreadRepository threadRepository;

	@Override
	public List<Thread> selectNotEmptyThreadList(Channel channel) {
		var thread = QThread.thread;

		// 메세지가 비어있지 않은 해당 채널의 쓰레드 목록
		var predicate = thread
			.channel.eq(channel)
			.and(thread.message.isNotEmpty());
		var threads = threadRepository.findAll(predicate);

		return IteratorAdapter.asList(threads.iterator());
	}

	@Override
	public Thread insert(Thread thread) {
		return threadRepository.save(thread);
	}


}
