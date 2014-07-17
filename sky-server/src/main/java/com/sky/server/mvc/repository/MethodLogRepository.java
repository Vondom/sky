package com.sky.server.mvc.repository;

import com.sky.server.mvc.model.MethodLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jcooky on 2014. 7. 9..
 */
@Repository
public interface MethodLogRepository extends JpaRepository<MethodLog, Long> {

}
