package com.sky.server.mvc.service;

import com.sky.server.mvc.model.Work;
import com.sky.server.mvc.repository.ProjectRepository;
import com.sky.server.mvc.repository.WorkRepository;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jcooky on 2014. 8. 7..
 */
@Service
public class WorkService {
  public static final int TIME_OUT = 1000 * 60 * 5;
  private static final Logger logger = LoggerFactory.getLogger(WorkService.class);

  @Autowired
  private WorkRepository workRepository;

  @Autowired
  private WorkerService workerService;

  @Autowired
  private ProfileService profileService;

  @Autowired
  private ProjectRepository projectRepository;

  @Transactional
  public Work create(Work work) throws TException {
    work.setProject(projectRepository.findOne(work.getProject().getId()));
    work.setOrdering(workRepository.count());
    work.setProfile(profileService.create());
    work = workRepository.save(work);

    workerService.doWork(work);

    return work;
  }

  public Work get(long id) {
    return workRepository.findOne(id);
  }
}
