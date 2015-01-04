<nav id="nav" class="navbar navbar-default" role="navigation">
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
      <ul id="nav-right" ng-controller="NavSigninCtrl" class="nav navbar-nav navbar-right">
        <li class="{{isSigned ? 'hide' : 'show'}}">
          <form id="form-signin" action="${rc.contextPath}/signin/github" method="POST">
            <button type="submit" class="navbar-btn btn btn-social btn-github">
              <i class="fa fa-github"></i>Sign in with GitHub
            </button>
            <input type="hidden" name="scope" value="user,read:org,repo"/>
          </form>
        </li>
        <li id="signed" class="{{isSigned ? 'show' : 'hide'}}">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
            {{user.name}}
            <img src="{{user.imageUrl}}">
            <span class="caret"></span>
          </a>
          <ul class="dropdown-menu" role="menu">
            <li><a href="/signout">SignOut</a></li>
          </ul>
        </li>
      </ul>
    </div>
  </div>
</nav>
<script>
  $(function () {
    $("#anchor-signin").click(function () {
      $("#form-signin").submit();
    })
  });
</script>