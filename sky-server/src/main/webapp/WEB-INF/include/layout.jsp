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
  <script type="text/javascript" src="${pageContext.request.contextPath}/vendor/jquery/dist/jquery.min.js"></script>
</head>
<body>
<tiles:insertAttribute name="body" defaultValue="" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
<script src="${pageContext.request.contextPath}/vendor/lodash/dist/lodash.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/bootstrap/dist/js/bootstrap.min.js"></script>
<jsp:include page="scripts.jsp" />
</body>
</html>
