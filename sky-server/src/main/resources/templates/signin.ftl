<#include "/include/header.ftl">
<style>
  html, body {
    height: 100%;
  }

  .container {
    position: relative;
    height: 100%;
    text-align: center;
  }

  .container .form-area {
    position: absolute;
    top: 25%;
    left: 50%;
    margin-left: -243px;
  }
</style>
<form action="${rc.contextPath}/signin/github" method="POST" class="container">
  <span class="form-area">
    <img src="${rc.contextPath}/img/logo.jpg">
    <br>
    <button type="submit" class="btn btn-social btn-lg btn-github">
      <i class="fa fa-github"></i>Sign in with GitHub
    </button>
    <input type="hidden" name="scope" value="user,read:org,repo" />
  </span>
</form>
<#include "/include/footer.ftl">