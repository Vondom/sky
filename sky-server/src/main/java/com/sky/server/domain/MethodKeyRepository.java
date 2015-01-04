package com.sky.server.domain;

import com.sky.commons.domain.MethodKey;
import com.sky.server.domain.custom.MethodKeyRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jcooky on 2014. 7. 9..
 */
@Repository
public interface MethodKeyRepository extends JpaRepository<MethodKey, Long>, MethodKeyRepositoryCustom {
}
