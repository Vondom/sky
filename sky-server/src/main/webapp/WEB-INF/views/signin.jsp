<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:insertTemplate template="/WEB-INF/include/layout.jsp">
  <tiles:putAttribute name="body">
    <form action="${pageContext.request.contextPath}/signin/github" method="POST" class="container">
        <span class="form-area">
          <img src="${pageContext.request.contextPath}/resources/img/logo.jpg">
          <br>
          <button type="submit" class="btn btn-social btn-lg btn-github">
            <i class="fa fa-github"></i>Sign in with GitHub
          </button>
          <input type="hidden" name="scope" value="user,read:org,repo" />
        </span>
    </form>
    <style>
      html, body {
        height: 100%;
      }

      body .container {
        position: relative;
        height: 100%;
        text-align: center;
      }

      body .container .form-area {
        position: absolute;
        top: 25%;
        left: 50%;
        margin-left: -243px;
      }

    </style>
  </tiles:putAttribute>
</tiles:insertTemplate>