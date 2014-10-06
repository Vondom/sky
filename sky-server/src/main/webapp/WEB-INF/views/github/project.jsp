<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<tiles:insertTemplate template="/WEB-INF/include/layout.jsp">
  <tiles:putAttribute name="body">
    <div class="container-fluid">
      <section class="row">
        <table id="projects" class="table table-bordered">
          <thead>
            <th>name</th>
          </thead>
          <tbody></tbody>
        </table>
      </section>
    </div>
    <script>
      $(function () {
        var $projects = $("#projects");
        $.ajax({
          url: sky.REQUEST_CONTEXT_PATH + "/api/project/github",
          type: "GET"
        }).done(function (projects) {
          console.debug(projects);
          _.forEach(projects, function (project) {
            $("<tr />", { text: project.name }).appendTo($projects.find('tbody'));
          });
        });
      });
    </script>
  </tiles:putAttribute>
</tiles:insertTemplate>

