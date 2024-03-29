package me.springstudy.jpastudy.thread;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.springstudy.jpastudy.channel.Channel;
import me.springstudy.jpastudy.comment.Comment;
import me.springstudy.jpastudy.common.TimeStamp;
import me.springstudy.jpastudy.emotion.ThreadEmotion;
import me.springstudy.jpastudy.mention.ThreadMention;
import me.springstudy.jpastudy.user.User;

// lombok
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

// jpa
@Entity
@Table(name = "TB_THREAD")
public class Thread extends TimeStamp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	/**
	 * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
	 */
	@Column(length = 500)
	private String message;

	/**
	 * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
	 */
	@Builder
	public Thread(String message) {
		this.message = message;
	}

	/**
	 * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
	 */
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	// FetchType = eager 인 경우, left join 을 하지 않아도 됨 → user 정보를 무조건 조회 해오기 때문에

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_id")
	private Channel channel;

	@OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Comment> comments = new LinkedHashSet<>();

	@OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ThreadMention> mentions = new LinkedHashSet<>();

	@OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ThreadEmotion> threadEmotions = new LinkedHashSet<>();

	/**
	 * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
	 */
	public void setChannel(Channel channel) {
		this.channel = channel;
		channel.addThread(this);
	}

	public void addMention(User user) {
		ThreadMention mention = ThreadMention.builder().user(user).thread(this).build();
		this.mentions.add(mention);
		user.getThreadMentions().add(mention);
	}

	public void addEmotion(User user, String body) {
		ThreadEmotion emotion = ThreadEmotion.builder().user(user).thread(this).body(body).build();
		this.threadEmotions.add(emotion);
	}

	public void addComment(Comment comment) {
		this.comments.add(comment);
		comment.setThread(this);
	}

	/**
	 * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
	 */
}
