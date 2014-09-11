package com.sky.server.config.annotation;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * Created by jcooky on 2014. 8. 27..
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public @interface TService {
  String name();
  Class<?> thrift();
}
