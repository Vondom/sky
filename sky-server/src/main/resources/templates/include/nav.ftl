<nav class="navbar navbar-default" role="navigation">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="/">Sky Engine</a>
    </div>
    <div class="collapse navbar-collapse" id="navbar-collapse">
      <ul class="nav navbar-nav">
        <li>
          <a href="${rc.contextPath}/project">Project</a>
        </li>
        <li>
          <a href="${rc.contextPath}/worker">Worker</a>
        </li>
      </ul>
      <ul id="nav-signout" class="nav navbar-nav navbar-right">
        <li><a href="${rc.contextPath}/signout">signout</a></li>
      </ul>
    </div>
  </div>
</nav>
<script>
  $(function () {
    if (_.isNull(sky.ACCESS_TOKEN)) {
      $("#nav-signout").hide();
    }
  });
</script>