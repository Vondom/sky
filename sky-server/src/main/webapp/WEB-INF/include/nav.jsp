<%--
  Created by IntelliJ IDEA.
  User: jcooky
  Date: 2014. 7. 26.
  Time: 오전 12:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-default" role="navigation">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="${pageContext.request.contextPath}/">Sky</a>
    </div>
    <div class="collapse navbar-collapse" id="navbar-collapse">
      <ul class="nav navbar-nav">
        <li class='dropdown'>
          <a class="dropdown-toggle" data-toggle="dropdown" href="#">Project <span class="caret"></span></a>
          <ul class="dropdown-menu" role="menu">
            <li><a href="${pageContext.request.contextPath}/project">All Projects</a></li>
            <li><a href="${pageContext.request.contextPath}/project/create">Create</a></li>
          </ul>
        </li>
      </ul>
    </div>
  </div>
</nav>