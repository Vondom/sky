package com.sky.server.config;

import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.sky.server.web.interceptor.AttributeInterceptor;
import com.sky.server.web.interceptor.UserInterceptor;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.ajp.AjpNioProtocol;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;

import javax.servlet.ServletContext;
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

  @Bean
  public EmbeddedServletContainerFactory servletContainer() {
    TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory(8080);
    tomcat.setProtocol(Http11NioProtocol.class.getName());

    Connector connector = new Connector(AjpNioProtocol.class.getName());
    connector.setScheme("ajp");
    connector.setPort(8009);
    tomcat.addAdditionalTomcatConnectors(connector);

    tomcat.getTomcatConnectorCustomizers().add(new TomcatConnectorCustomizer() {
      @Override
      public void customize(Connector connector) {
        connector.setMaxPostSize(Integer.MAX_VALUE);
      }
    });

    return tomcat;
  }

  @Bean
  public TilesConfigurer tilesConfigurer(ServletContext servletContext) throws ClassNotFoundException {
    Class.forName("org.apache.jasper.compiler.JspRuntimeContext");
    TilesConfigurer tilesConfigurer = new TilesConfigurer();
    tilesConfigurer.setServletContext(servletContext);
    tilesConfigurer.setCompleteAutoload(true);

    return tilesConfigurer;
  }

//  @Override
//  public void addResourceHandlers(ResourceHandlerRegistry registry) {
//    registry.addResourceHandler("/**").addResourceLocations("/");
//  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    super.addInterceptors(registry);

    registry.addWebRequestInterceptor(attributeInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns("/resources/**");
    registry.addInterceptor(userInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns("/resources/**")
        .excludePathPatterns("/agent/**")
        .excludePathPatterns("/test/**")
        .excludePathPatterns("/download/**");
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

    registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    registry.addResourceHandler("/**").addResourceLocations("/");
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    super.addViewControllers(registry);

    registry.addViewController("/signin").setViewName("/signin");
    registry.addViewController("/signout");
  }
}
