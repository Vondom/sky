package com.sky.server.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.*;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class MappedRequestPathMatcherTest {

  private MappedRequestPathMatcher pathMatcher;

  private MockServletContext servletContext;

  @Before
  public void setUp() throws Exception {
    servletContext = new MockServletContext("/");
    pathMatcher = new MappedRequestPathMatcher(new AntPathMatcher(), ArrayUtils.toArray(
        "/**"
    ), ArrayUtils.toArray(
        "/css/**", "/js/**"
    ));
  }

  @Test
  public void testMatches() throws Exception {
    HttpServletRequest request = MockMvcRequestBuilders.get("/css/style.css").buildRequest(servletContext);

    assertThat(pathMatcher.matches(request), is(false));

    request = MockMvcRequestBuilders.get("/edit").buildRequest(servletContext);
    assertThat(pathMatcher.matches(request), is(true));
  }
}