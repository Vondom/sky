package com.sky.server.mvc.service;

import com.sky.commons.Jar;
import com.sky.server.mvc.model.Work;
import com.sky.server.mvc.repository.ProjectRepository;
import com.sky.server.mvc.repository.WorkRepository;
import com.sky.server.mvc.repository.WorkerRepository;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jcooky on 2014. 8. 7..
 */
@Service
public class WorkService {
  public static final int TIME_OUT = 1000 * 60 * 5;

  @Autowired
  private WorkRepository workRepository;

  @Autowired
  private WorkerRepository workerRepository;

  @Autowired
  private com.sky.commons.Worker.Iface worker;

  @Autowired
  private ProjectRepository projectRepository;

  @Transactional
  public Work create(Work work) throws TException {
    work.setProject(projectRepository.findOne(work.getProject().getId()));
    work.setOrdering(workRepository.count());
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

  }

  public Work get(long id) {
    return workRepository.findOne(id);
  }
}
