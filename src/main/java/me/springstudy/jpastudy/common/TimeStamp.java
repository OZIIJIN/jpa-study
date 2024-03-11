package me.springstudy.jpastudy.common;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import me.springstudy.jpastudy.user.User;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class TimeStamp {

	private LocalDateTime createdAt;

	private LocalDateTime modifiedAt;

	@CreatedBy
	@ManyToOne
	private User createdBy;

	@LastModifiedBy
	@ManyToOne
	private User modifiedBy;

	public void updateCreatedAt() {
		this.createdAt = LocalDateTime.now();
	}

	public void updateModifiedAt() {
		this.modifiedAt = LocalDateTime.now();
	}

	public void updateCreatedBy() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			this.createdBy = null;
		}
		this.createdBy = (User) authentication.getPrincipal();

	}

	public void updateModifiedBy() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			this.modifiedBy = null;
		}
		this.modifiedBy = (User) authentication.getPrincipal();

	}

}
