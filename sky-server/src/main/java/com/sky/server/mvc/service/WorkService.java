package com.sky.server.mvc.service;

import com.sky.commons.Jar;
import com.sky.server.mvc.model.Work;
import com.sky.server.mvc.repository.ProjectRepository;
import com.sky.server.mvc.repository.WorkRepository;
import com.sky.server.mvc.repository.WorkerRepository;
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
  private WorkerRepository workerRepository;

  @Autowired
  private ProfileService profileService;

  @Autowired
  private com.sky.commons.Worker.Iface worker;

  @Autowired
  private ProjectRepository projectRepository;

  @Transactional
  public Work create(Work work) throws TException {
    work.setProject(projectRepository.findOne(work.getProject().getId()));
    work.setOrdering(workRepository.count());
    work.setProfile(profileService.create());
    work = workRepository.save(work);

    worker.doWork(toWork(work));

    return work;
  }

  private com.sky.commons.Work toWork(Work work) {
    return new com.sky.commons.Work()
        .setId(work.getId())
        .setJar(new Jar()
            .setFile(work.getProject().getJarFile())
            .setName(work.getProject().getJarFileName()))
        .setArguments(work.getProject().getArguments())
        .setProjectId(work.getProject().getId());
  }

  public void done(com.sky.commons.Work work) {
    logger.debug("DONE workId: {}", work.getId());
  }

  public Work get(long id) {
    return workRepository.findOne(id);
  }
}
