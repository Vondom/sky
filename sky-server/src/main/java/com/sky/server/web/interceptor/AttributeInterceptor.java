package com.sky.server.web.interceptor;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * Created by jcooky on 2014. 7. 10..
 */
@Component
public class AttributeInterceptor implements WebRequestInterceptor {

  @Autowired
  private Connection<?> connection;

  @Override
  public void preHandle(WebRequest request) throws Exception {
    request.setAttribute("lang", request.getLocale().getLanguage(), RequestAttributes.SCOPE_REQUEST);
    request.setAttribute("accessToken", defaultIfEmpty(String.valueOf(request.getAttribute("accessToken", RequestAttributes.SCOPE_REQUEST)), ""), RequestAttributes.SCOPE_REQUEST);
  }

  @Override
  public void postHandle(WebRequest request, ModelMap model) throws Exception {

  }

  @Override
  public void afterCompletion(WebRequest request, Exception ex) throws Exception {

  }
}
