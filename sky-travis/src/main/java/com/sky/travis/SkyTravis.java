package com.sky.travis;

import com.google.common.base.Splitter;
import com.sky.commons.domain.ExecutionUnit;
import com.sky.commons.domain.Work;
import com.sky.travis.domain.ExecutionUnitRepository;
import com.sky.travis.domain.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

/**
 * Created by JCooky on 15. 1. 24..
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class SkyTravis implements CommandLineRunner {

  @Autowired
  private ExecutionUnitRepository executionUnitRepository;
  @Autowired
  private WorkRepository workRepository;

  @Autowired
  private Environment env;

  @Override
  public void run(String... args) throws Exception {
    List<String> profiles = Arrays.asList(env.getActiveProfiles());

    if (!profiles.contains("test")) {
      String repoSlug = System.getenv("TRAVIS_REPO_SLUG");
      String branch = System.getenv("TRAVIS_BRANCH");

      List<String> slug = Splitter.on('/').splitToList(repoSlug);
      String account = slug.get(0), project = slug.get(1);

      ExecutionUnit executionUnit = executionUnitRepository.get(account, project, branch);
      if (executionUnit == null)
        throw new IllegalArgumentException();

      Work work = new Work();

      work.setExecutionUnit(executionUnit);
      workRepository.create(work);
    }
  }

  public static void main(String []args) {
    new SpringApplicationBuilder()
        .showBanner(true)
        .web(false)
        .addCommandLineProperties(false)
        .sources(SkyTravis.class)
        .run(args);
  }
}
