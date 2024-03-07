package me.springstudy.jpastudy.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.springstudy.jpastudy.channel.Channel;
import me.springstudy.jpastudy.userchannel.UserChannel;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
	@PersistenceContext
	EntityManager entityManager;

	public User insertUser (User user) {
		entityManager.persist(user);
		return user;
	}

	public User selectUserChannel (Long id) {
		return entityManager.find(User.class, id);
	}
}
