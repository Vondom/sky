package com.sky.server.mvc.repository;

import com.sky.server.mvc.model.ExecutionUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jcooky on 2014. 10. 17..
 */
@Repository
public interface ExecutionUnitRepository extends JpaRepository<ExecutionUnit, Long> {
}
