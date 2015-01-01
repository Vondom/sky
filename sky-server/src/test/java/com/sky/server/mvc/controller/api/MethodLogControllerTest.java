package com.sky.server.mvc.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.commons.model.ClassKey;
import com.sky.commons.model.MethodKey;
import com.sky.commons.model.MethodLog;
import com.sky.commons.model.Work;
import com.sky.server.mvc.repository.WorkRepository;
import com.sky.server.test.SpringBasedTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.transaction.TestContextTransactionUtils;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class MethodLogControllerTest extends SpringBasedTestSupport {

  @Autowired
  private MethodLogController methodLogController;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private WorkRepository workRepository;

  @Autowired
  private PlatformTransactionManager tm;
  /*
null
0
1419996714581
-1
main
MethodKey{id=0, name='acquire', signature='()V'}
  SimpleSemaphore
  com.sky.sample
MethodKey{id=0, name='process', signature='(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V'}
  PingPongRight
  com.sky.sample
0
null
*/

  @Test
  public void testCreate() throws Exception {
    Work work = new Work();
    work = workRepository.save(work);

    MethodKey callerKey = new MethodKey();
    callerKey.setName("acquire");
    callerKey.setSignature("()V");
    callerKey.setClassKey(new ClassKey("SimpleSemaphore", "com.sky.sample"));

    MethodKey calleeKey = new MethodKey();
    calleeKey.setName("process");
    calleeKey.setSignature("(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V");
    calleeKey.setClassKey(new ClassKey("PingPongRight", "com.sky.sample"));

    MethodLog methodLog = new MethodLog();
    methodLog.setCaller(callerKey);
    methodLog.setMethodKey(calleeKey);
    methodLog.setStartTime(1419996714581L);
    methodLog.setOrdering(-1);
    methodLog.setElapsedTime(0);
    methodLog.setTotalElapsedTime(0);
    methodLog.setThreadName("main");
    Work work1 = new Work();
    work1.setId(work.getId());
    methodLog.setWork(work1);

    MockMvcBuilders.standaloneSetup(methodLogController).build()
        .perform(MockMvcRequestBuilders
                .post("/api/method-log")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
            .content(mapper.writeValueAsString(methodLog))
        ).andExpect(MockMvcResultMatchers.status().isOk());


    TransactionStatus ts = tm.getTransaction(new DefaultTransactionDefinition());
    try {
      work = workRepository.findOne(work.getId());
      assertThat(work.getMethodLogs().size(), is(1));
    } finally {
      tm.rollback(ts);
    }
  }
}