package me.springstudy.jpastudy.thread;

import static me.springstudy.jpastudy.thread.QThread.thread;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class ThreadRepositoryQueryImpl implements ThreadRepositoryQuery {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<Thread> search(ThreadSearchCond cond, Pageable pageable) {
		var query = query(thread, cond)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize());

		query.orderBy(thread.mentions.any().createdAt.desc());

		var threads = query.fetch();
		long totalSize = countQuery(cond).fetch().get(0);

		threads.stream()
			.map(Thread::getComments)
			.forEach(comments -> comments
				.forEach(comment -> Hibernate.initialize(comment.getEmotions())));

		return PageableExecutionUtils.getPage(threads, pageable,
			() -> totalSize); // page 객체로 감싸서 return
	}

	private <T> JPAQuery<T> query(Expression<T> expr, ThreadSearchCond cond) {
		return jpaQueryFactory.select(expr)
			.from(thread)
			.leftJoin(thread.channel).fetchJoin() // 기본 LAZY를 설정한 뒤에 필요할때만 fetch Join 을 수행한다.
			.leftJoin(thread.threadEmotions).fetchJoin()
			.leftJoin(thread.comments).fetchJoin()
			.where(
				channelIdEq(cond.getChannelId()),
				mentionedUserIdEq(cond.getMentionedUserId())
			);
	}

	private JPAQuery<Long> countQuery(ThreadSearchCond cond) {
		return jpaQueryFactory.select(Wildcard.count)
			.from(thread)
			.where(
				channelIdEq(cond.getChannelId()),
				mentionedUserIdEq(cond.getMentionedUserId())
			);
	}

	// channelId가 빈값이면 쿼리에서 에러가 나기 때문에 에러가 나지 않도록 처리
	private BooleanExpression channelIdEq(Long channelId) {
		return Objects.nonNull(channelId) ? thread.channel.id.eq(channelId) : null;
	}

	private BooleanExpression mentionedUserIdEq(Long mentionedUserId) {
		return Objects.nonNull(mentionedUserId) ? thread.mentions.any().user.id.eq(mentionedUserId)
			: null;
	}
}
