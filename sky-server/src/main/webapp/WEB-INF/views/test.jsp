<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:insertTemplate template="/WEB-INF/include/layout.jsp">
  <tiles:putAttribute name="body">
    <a class="btn btn-block btn-social btn-lg btn-github">
      <i class="fa fa-github"></i>Sign in with GitHub
    </a>
  </tiles:putAttribute>
</tiles:insertTemplate>