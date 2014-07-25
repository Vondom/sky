<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:insertTemplate template="/WEB-INF/include/layout.jsp">
  <tiles:putAttribute name="body">
    <div id="projects" class="container-fluid" data-user-id="${userId}">
      <div class="row">

      </div>
    </div>
  </tiles:putAttribute>
</tiles:insertTemplate>