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
<html lang="${lang}">
<head>
  <title>Sky Engine</title>
  <meta charset="utf-8">
  <script type="text/javascript" src="${pageContext.request.contextPath}/resources/vendor/jquery/dist/jquery.min.js"></script>
  <script type="text/javascript"
          src='https://www.google.com/jsapi?autoload={"modules":[{"name":"visualization","version":"1","packages":["corechart","table"]}]}'>
  </script>
</head>
<body>
<jsp:include page="nav.jsp" />
<tiles:insertAttribute name="body" defaultValue="" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/style.css">
<jsp:include page="scripts.jsp" />
</body>
</html>
