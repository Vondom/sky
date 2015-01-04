package com.sky.server.domain;

import com.sky.commons.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jcooky on 2014. 7. 28..
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
