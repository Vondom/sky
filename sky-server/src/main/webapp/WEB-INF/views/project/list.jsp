<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:insertTemplate template="/WEB-INF/include/layout.jsp">
  <tiles:putAttribute name="body">
    <div id="projects" class="container-fluid" data-user-id="${userId}">
      <div class="row">
        <a href="${pageContext.request.contextPath}/project/create" role="button" class="btn btn-md btn-success pull-right">
          Create
        </a>
      </div>
      <section class="row">

      </section>
    </div>
    <script>
      $(function () {
        var $projects = $("#projects");
        $.ajax({
          url: [sky.API_USER_URL, "/", $projects.data("user-id"), "/projects"].join(''),
          type: "GET",
          success: function (projects) {
            var $thead = $("<thead />"),
                $tbody = $("<tbody />");

            $("<tr />", {
              html: [
                $("<th />", { text: "Id" }),
                $("<th />", { text: "Name" }),
                $("<th />", { text: "Actions" })
              ]
            }).appendTo($thead);

            _.forEach(projects, function (project) {
              $("<tr />", {
                html: [
                  $("<td />", { text: project.id }),
                  $("<td />", { text: project.name }),
                  $("<td />", {
                    html: $("<button />", {
                      "class": "btn btn-xs btn-primary",
                      text: "Go!",
                      on: {
                        click: function (e) {
                          $.ajax({
                            url: sky.API_WORK_URL,
                            type: "POST",
                            data: JSON.stringify({
                              project: {
                                id: project.id
                              }
                            })
                          }).done(function (work) {
                            console.debug(work);

                            location.href = "/work/profile/"+work.id;
                          });

                          e.stopPropagation();
                        }
                      }
                    })
                  })
                ]
              }).appendTo($tbody);
            });

            $("<table />", {
              "class": "table table-striped table-bordered table-hover table-condensed"
            }).append($thead, $tbody).appendTo($projects.find('section.row'));
          },
          error: function (jqAjax) {
            console.error(jqAjax);
          }
        })
      });
    </script>
  </tiles:putAttribute>
</tiles:insertTemplate>