package com.sky.server.mvc.repository;

import com.sky.server.mvc.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jcooky on 2014. 7. 8..
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

}
