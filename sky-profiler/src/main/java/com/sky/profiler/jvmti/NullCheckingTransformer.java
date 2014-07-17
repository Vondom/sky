package com.sky.profiler.jvmti;

import org.shiftone.jrat.util.log.Logger;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * Created by jcooky on 2014. 6. 22..
 */
public class NullCheckingTransformer implements ClassFileTransformer {

  private ClassFileTransformer transformer;
  private static final Logger LOGGER = Logger.getLogger(NullCheckingTransformer.class);

  public NullCheckingTransformer(ClassFileTransformer transformer) {
    this.transformer = transformer;
  }

  @Override
  public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
    return className != null ? transformer.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer) : classfileBuffer;
  }
}
