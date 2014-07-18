<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:insertTemplate template="/WEB-INF/include/layout.jsp">
  <tiles:putAttribute name="body">
    <div id="method-logs" data-profile-id="${profileId}" class="container">
      <div class="row">
        <div id="scatter"></div>
      </div>
      <div class="row">
        <div></div>
      </div>
    </div>
  </tiles:putAttribute>
</tiles:insertTemplate>