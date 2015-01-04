package com.sky.server.config;

import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.sky.server.config.interceptor.AttributeInterceptor;
import com.sky.server.config.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import java.util.List;

/**
 * Created by jcooky on 2014. 6. 16..
 */
@Configuration
public class WebMvcConfig extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter {

  @Autowired
  private AttributeInterceptor attributeInterceptor;

  @Autowired
  private UserInterceptor userInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    super.addInterceptors(registry);

    registry.addWebRequestInterceptor(attributeInterceptor)
        .addPathPatterns("/**");
    registry.addInterceptor(userInterceptor)
        .addPathPatterns("/**");
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    super.configureMessageConverters(converters);
    for (HttpMessageConverter<?> converter : converters) {
      if (converter instanceof MappingJackson2HttpMessageConverter) {
        MappingJackson2HttpMessageConverter converter1 = (MappingJackson2HttpMessageConverter)converter;
        converter1.getObjectMapper().registerModule(new Hibernate4Module());
      }
    }

  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    super.addResourceHandlers(registry);
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    super.addViewControllers(registry);

    registry.addViewController("/").setViewName("index");
    registry.addViewController("/signin").setViewName("signin");
    registry.addRedirectViewController("/signout", "/");
  }
}
