package me.springstudy.jpastudy.thread;

import com.mysema.commons.lang.IteratorAdapter;
import com.querydsl.core.types.Predicate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.springstudy.jpastudy.user.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ThreadServiceImpl implements ThreadService {

	private final ThreadRepository threadRepository;

	@Override
	public List<Thread> selectMentionedThreadList(User user) {
		// Thread가 필요함
		QThread thread = QThread.thread;

		Predicate predicate = thread.mentions.any().user.eq(user);
		var threads = threadRepository.findAll(predicate);

		return IteratorAdapter.asList(threads.iterator());
	}

	@Override
	public Thread insert(Thread thread) {
		return threadRepository.save(thread);
	}

}
