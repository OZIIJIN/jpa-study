package me.springstudy.jpastudy.thread;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ThreadRepositoryQuery {

	// QueryRepository의 경우 search를 넣어 컨디션 객체를 정의
	Page<Thread> search(ThreadSearchCond cond, Pageable pageable);

}
