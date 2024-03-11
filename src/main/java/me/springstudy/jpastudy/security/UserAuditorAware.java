package me.springstudy.jpastudy.security;

import java.util.Optional;
import me.springstudy.jpastudy.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserAuditorAware implements AuditorAware<User> {

	@Override
	public Optional<User> getCurrentAuditor() { //AuditorAware의 getCurrentAuditor() 메서드를 구현
		Authentication authentication = SecurityContextHolder.getContext()
			.getAuthentication(); //인증정보를 받아옴

		if (authentication == null || !authentication.isAuthenticated()) {
			return Optional.empty();
		}

		return Optional.of(((UserDetailsImpl) authentication.getPrincipal()).getUser());
	}
}
