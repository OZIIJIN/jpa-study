package me.springstudy.jpastudy.userchannel;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import me.springstudy.jpastudy.channel.Channel;
import org.springframework.stereotype.Repository;

@Repository
public class UserChannelRepository {

	@PersistenceContext
	EntityManager entityManager;

	public UserChannel insertUserChannel (UserChannel userchannel) {
		entityManager.persist(userchannel);
		return userchannel;
	}

	public UserChannel selectUserChannel (Long id) {
		return entityManager.find(UserChannel.class, id);
	}
}
