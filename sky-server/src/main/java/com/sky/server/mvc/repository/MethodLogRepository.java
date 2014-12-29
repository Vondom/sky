package com.sky.server.mvc.repository;

import com.sky.commons.model.MethodLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jcooky on 2014. 7. 9..
 */
@Repository
public interface MethodLogRepository extends JpaRepository<MethodLog, Long> {

}
