package com.sky.server.mvc.repository;

import com.sky.server.mvc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jcooky on 2014. 7. 21..
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByEmail(String email);
}
