<!DOCTYPE HTML>
<html lang="${lang}" data-context-path="${rc.contextPath}" data-user-id="${userId}" data-access-token="${accessToken}" ng-app="skyApp">
<head>
  <title>Sky Engine</title>
  <meta charset="utf-8">
  <script src="${rc.contextPath}/vendor/lodash/dist/lodash.min.js"></script>
  <script type="text/javascript" src="${rc.contextPath}/vendor/jquery/dist/jquery.min.js"></script>
  <script type="text/javascript"
          src='https://www.google.com/jsapi?autoload={"modules":[{"name":"visualization","version":"1","packages":["corechart","table"]}]}'>
  </script>
  <#include "scripts.ftl">
  <#include "styles.ftl">
</head>
<body>
<#include "nav.ftl">
