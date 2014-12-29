package com.sky.server.mvc.controller.api;

import com.sky.commons.model.ExecutionUnit;
import com.sky.commons.model.Work;
import com.sky.server.mvc.repository.ExecutionUnitRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * Created by jcooky on 2014. 7. 25..
 */
@RestController
@RequestMapping(value = "/api/execution-unit")
public class ExecutionUnitController {
  private static final Logger logger = LoggerFactory.getLogger(ExecutionUnitController.class);

  @Autowired
  private ExecutionUnitRepository executionUnitRepository;


  @RequestMapping(method = RequestMethod.POST)
  @Transactional
  public ExecutionUnit create(@RequestBody ExecutionUnit executionUnit) {
    logger.trace("{}", executionUnit.getProject());

    return executionUnitRepository.save(executionUnit);
  }

  @RequestMapping(method = RequestMethod.GET)
  public List<ExecutionUnit> list() {
    return executionUnitRepository.findAll();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ExecutionUnit get(@PathVariable long id) {
    return executionUnitRepository.findOne(id);
  }

  @RequestMapping(value = "/{id}/works", method = RequestMethod.GET)
  public Collection<? extends Work> getWorks(@PathVariable long id) {
    logger.trace(".getWorks(id={})", id);

    Collection<? extends Work> works = executionUnitRepository.findOne(id).getWorks();
    logger.trace("works: {}", works);

    return works;
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @Transactional
  public void remove(@PathVariable long id) {
    executionUnitRepository.delete(id);
  }

  @ExceptionHandler(UnsupportedOperationException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public String handleException(UnsupportedOperationException e) {
    return ObjectUtils.defaultIfNull(e.getLocalizedMessage(), e.getMessage());
  }

}
