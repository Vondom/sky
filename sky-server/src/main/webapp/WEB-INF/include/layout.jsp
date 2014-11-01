<%--
  Created by IntelliJ IDEA.
  User: jcooky
  Date: 2014. 7. 10.
  Time: 오전 4:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!DOCTYPE HTML>
<html lang="${lang}" data-context-path="${pageContext.request.contextPath}" data-access-token="${accessToken}" ng-app="skyApp">
<head>
  <title>Sky Engine</title>
  <meta charset="utf-8">
  <script src="${pageContext.request.contextPath}/resources/vendor/lodash/dist/lodash.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/resources/vendor/jquery/dist/jquery.min.js"></script>
  <script type="text/javascript"
          src='https://www.google.com/jsapi?autoload={"modules":[{"name":"visualization","version":"1","packages":["corechart","table"]}]}'>
  </script>
  <jsp:include page="scripts.jsp" />
</head>
<body>
<jsp:include page="nav.jsp" />
<tiles:insertAttribute name="body" defaultValue="" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/style.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/vendor/bootstrap-switch/dist/css/bootstrap3/bootstrap-switch.min.css">
</body>
</html>
