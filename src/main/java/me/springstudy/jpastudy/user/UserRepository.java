package me.springstudy.jpastudy.user;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u, u.password AS customField FROM User u WHERE u.username = ?1")
	List<User> findByUsername(String username, Sort sort);
	// From + Entity 이름
}
