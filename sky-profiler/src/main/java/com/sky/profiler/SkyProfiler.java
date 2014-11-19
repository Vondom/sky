package com.sky.profiler;

import com.sky.profiler.jvmti.NullCheckingTransformer;
import org.shiftone.jrat.core.Environment;
import org.shiftone.jrat.core.Mode;
import org.shiftone.jrat.core.config.Configuration;
import org.shiftone.jrat.core.config.ConfigurationParser;
import org.shiftone.jrat.core.criteria.AndMethodCriteria;
import org.shiftone.jrat.core.criteria.MethodCriteria;
import org.shiftone.jrat.inject.InjectorOptions;
import org.shiftone.jrat.jvmti.FilterClassFileTransformer;
import org.shiftone.jrat.jvmti.InjectClassFileTransformer;
import org.shiftone.jrat.jvmti.SystemPropertyTweakingTransformer;
import org.shiftone.jrat.jvmti.TryCatchClassFileTransformer;
import org.shiftone.jrat.util.VersionUtil;
import org.shiftone.jrat.util.log.Logger;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.net.URL;

/**
 * -javaagent:
 */
public class SkyProfiler {

  private static final Logger LOG = Logger.getLogger(SkyProfiler.class);
  private static boolean installed = false;
  private static Configuration configuration;

  public static void premain(String agentArgs, Instrumentation instrumentation) {
    Mode.set(Mode.RUNTIME);

    if (installed) {
      LOG.warn("one JRat Agent was already installed.");
      LOG.warn("your probably have the -javaagent arg on the command line twice");

      return;
    }

    LOG.info("Installing JRat " + VersionUtil.getVersion() + " ClassFileTransformer...");

    try {
      URL url = new URL(System.getProperty(Keys.CONFIG_KEY));
      configuration = ConfigurationParser.parse(url.openStream());
    } catch (IOException e) {
      LOG.error(e.getMessage(), e);
    }

    if (configuration == null) {
      // this must happen after the mode is set because it will
      // GET the mode locking it.
//      configuration = Environment.getConfiguration();
      LOG.error("configuration is null");
    } else {
      Environment.setConfiguration(configuration);
    }

    InjectorOptions injectorOptions = new InjectorOptions();
    AndMethodCriteria and = new AndMethodCriteria();
    and.addCriteria(configuration);
    and.addCriteria(new MethodCriteria() {
      @Override
      public boolean isMatch(String className, long modifiers) {
        return isMatch(className, null, null, modifiers);
      }

      @Override
      public boolean isMatch(String className, String methodName, String signature, long modifiers) {
        return !className.startsWith("com.sky.profiler") && !className.startsWith("com.sky.commons");
      }
    });

    injectorOptions.setCriteria(and);

    try {
      ClassFileTransformer transformer;

      transformer = new InjectClassFileTransformer(injectorOptions);
      transformer = new FilterClassFileTransformer(and, transformer);

      if (configuration.getSettings().isSystemPropertyTweakingEnabled()) {
        transformer = new SystemPropertyTweakingTransformer(transformer);
      }

      transformer = new TryCatchClassFileTransformer(transformer);

      transformer = new NullCheckingTransformer(transformer);

      // transformer = new DumpClassFileTransformer(transformer);
      instrumentation.addTransformer(transformer);
      installed = true;

      LOG.info("Installed " + transformer + ".");
      //redefineAllLoadedClasses(instrumentation, transformer);
    } catch (Throwable e) {
      LOG.info("Installed = " + installed, e);
    }
  }
}