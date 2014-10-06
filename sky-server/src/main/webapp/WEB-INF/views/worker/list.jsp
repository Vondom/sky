<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:insertTemplate template="/WEB-INF/include/layout.jsp">
  <tiles:putAttribute name="body">
    <div id="workers" class="container-fluid">
      <div class="row">
      </div>
      <section class="row col-md-8 col-md-offset-2">
      </section>
    </div>
    <script>
      $(function () {
        $.ajax({
          url: sky.API_WORKER_URL,
          type: "GET"
        }).done(function (workers) {
          var $table = $("<table />", {
            "class": "table",
            html: $("<tr />", {
              html: [$("<th />", { text: "#" }), $("<th />", { text: "Status" })]
            })
          }).appendTo($("#workers").find('section'));

          _.forEach(workers, function (worker) {
            $("<tr />", {
              html: [$("<td />", { text: worker.id }), $("<td />", { text: worker.state })]
            }).appendTo($table);
          });
        })
      });
    </script>
  </tiles:putAttribute>
</tiles:insertTemplate>