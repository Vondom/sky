package com.sky.server.domain;

import com.sky.commons.domain.MethodLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jcooky on 2014. 7. 9..
 */
@Repository
public interface MethodLogRepository extends JpaRepository<MethodLog, Long> {

}
