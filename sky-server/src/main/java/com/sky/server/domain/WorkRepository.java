package com.sky.server.domain;

import com.sky.commons.domain.Work;
import com.sky.server.domain.custom.WorkRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jcooky on 2014. 8. 4..
 */
@Repository
public interface WorkRepository extends JpaRepository<Work, Long>, WorkRepositoryCustom {
  Work findByWorker_id(long id);
}
