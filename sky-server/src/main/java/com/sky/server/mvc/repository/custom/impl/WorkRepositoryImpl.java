package com.sky.server.mvc.repository.custom.impl;

import com.mysema.query.jpa.impl.JPAQuery;
import com.sky.server.mvc.model.QWork;
import com.sky.server.mvc.model.Work;
import com.sky.server.mvc.repository.custom.WorkRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

/**
 * Created by jcooky on 2014. 8. 4..
 */
public class WorkRepositoryImpl implements WorkRepositoryCustom {

  @Autowired
  private EntityManager em;

  @Override
  public Work findWork() {

    QWork work = new QWork("work");

    return new JPAQuery(em)
        .from(work)
        .where(work.worker.isNull())
        .orderBy(work.ordering.desc())
        .singleResult(work);
  }

  @Override
  public Work findLastWork() {
    QWork work = new QWork("work");

    return new JPAQuery(em)
        .from(work)
        .where(work.worker.isNotNull())
        .orderBy(work.ordering.desc())
        .singleResult(work);
  }
}
