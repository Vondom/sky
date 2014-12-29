package com.sky.server.mvc.repository;

import com.sky.commons.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jcooky on 2014. 7. 28..
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
