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
      <ul id="nav-signout" class="nav navbar-nav navbar-right">
        <li><a href="${pageContext.request.contextPath}/signout">signout</a></li>
      </ul>
    </div>
  </div>
</nav>
<script>
  $(function () {
    if (sky.ACCESS_TOKEN == "") {
      $("#nav-signout").hide();
    }
  });
</script>