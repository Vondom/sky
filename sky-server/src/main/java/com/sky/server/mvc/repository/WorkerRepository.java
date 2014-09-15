package com.sky.server.mvc.repository;

import com.sky.server.mvc.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jcooky on 2014. 7. 30..
 */
@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
  Worker findByAddressAndPort(String address, int port);
  List<Worker> findByState(Worker.State state);
}
