package com.sky.server.mvc.repository;

import com.sky.server.mvc.model.Work;
import com.sky.server.mvc.repository.custom.WorkRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jcooky on 2014. 8. 4..
 */
@Repository
public interface WorkRepository extends JpaRepository<Work, Long>, WorkRepositoryCustom {
  Work findByWorker_id(long id);
}
