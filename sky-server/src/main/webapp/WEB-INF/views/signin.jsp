<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:insertTemplate template="/WEB-INF/include/layout.jsp">
  <tiles:putAttribute name="body">
    <form action="${pageContext.request.contextPath}/signin/github" method="POST">
      <button type="submit" class="btn btn-social btn-lg btn-github">
        <i class="fa fa-github"></i>Sign in with GitHub
      </button>
      <input type="hidden" name="scope" value="user:email" />
    </form>
  </tiles:putAttribute>
</tiles:insertTemplate>