package me.springstudy.jpastudy.thread;

import com.mysema.commons.lang.IteratorAdapter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.springstudy.jpastudy.channel.Channel;
import me.springstudy.jpastudy.common.PageDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional(readOnly = true)
	@Override
	public Page<Thread> selectMentionedThreadList(Long userId, PageDTO pageDTO) {
		var cond = ThreadSearchCond.builder().mentionedUserId(userId).build();
		return threadRepository.search(cond, pageDTO.toPageable());
	}

	@Override
	public Thread insert(Thread thread) {
		return threadRepository.save(thread);
	}


}
