package com.sky.server.domain;

import com.sky.commons.domain.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jcooky on 2014. 7. 30..
 */
@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {

}
