package me.springstudy.jpastudy.mention;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.springstudy.jpastudy.thread.Thread;
import me.springstudy.jpastudy.user.User;

// lombok
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

// jpa
@Entity
@Table(name = "TB_MENTION")
public class Mention { // 복합키로 설정 하기

	/**
	 * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
	 */
	@EmbeddedId
	private MentionId mentionId;

	/**
	 * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
	 */

	/**
	 * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
	 */
	@ManyToOne
	@MapsId("user_id")
	User user;

	@ManyToOne
	@MapsId("thread_id")
	Thread thread;

	/**
	 * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
	 */

	/**
	 * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
	 */
}
