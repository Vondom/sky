package com.sky.server.mvc.repository;

import com.sky.commons.model.ClassKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jcooky on 2014. 7. 9..
 */
@Repository
public interface ClassKeyRepository extends JpaRepository<ClassKey, Long> {
  ClassKey findByNameAndPackageName(String name, String packageName);
}
