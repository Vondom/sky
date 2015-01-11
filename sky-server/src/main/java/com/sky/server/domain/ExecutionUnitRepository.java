package com.sky.server.domain;

import com.sky.commons.domain.ExecutionUnit;
import com.sky.server.domain.custom.ExecutionUnitRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jcooky on 2014. 10. 17..
 */
@Repository
public interface ExecutionUnitRepository extends ExecutionUnitRepositoryCustom, JpaRepository<ExecutionUnit, Long> {

}
