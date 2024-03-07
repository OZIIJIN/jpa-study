package me.springstudy.jpastudy.user;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.springstudy.jpastudy.userchannel.UserChannel;

// lombok
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

// jpa
@Entity
@Table(name = "TB_USER")
public class User {
	/**
	 * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(length = 25)
	private String username;

	@Column(length = 25)
	private String password;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "street", column = @Column(name = "home_street"))
	}) // Address에 있는 "street" 값을 여기서는 "home_street" 로 저장하겠다.
	private Address address;


	/**
	 * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
	 */


	/**
	 * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
	 */
	 @OneToMany(mappedBy = "user")
	Set<UserChannel> userChannels = new LinkedHashSet<>();

	/**
	 * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
	 */


	/**
	 * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
	 */


}
