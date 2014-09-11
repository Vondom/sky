<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:insertTemplate template="/WEB-INF/include/layout.jsp">
  <tiles:putAttribute name="body">
    <div id="profile" data-profile-id="${profileId}" data-work-id="${workId}" class="container">
      <div class="row">
        <div id="scatter"></div>
      </div>
      <div class="row">
        <div></div>
      </div>
    </div>
    <script>
      //      (function ($, sky) {
      //        $(function () {
      //          var workId = $("#profile").data('work-id');
      //          $.ajax({
      //            url: sky.REQUEST_CONTEXT_PATH + "/api/work/" + workId + "/profile",
      //            type: 'GET',
      //            success: function (profiles) {
      ////          console.debug(profiles);
      //
      //              setTimeout(function () {
      //                var $profiles = $("#profiles"),
      //                    $table = $("<table />", {
      //                      "class": "table"
      //                    }).append(
      //                        $("<tr/>").append($("<th/>", {
      //                          text: "#"
      //                        })).append($("<th/>", {
      //                          text: "Action"
      //                        })).wrap("<thead></thead>")
      //                    ).append("<tbody />");
      //
      //                _.forEach(profiles, function (profile) {
      //                  $("<tr />").append($("<td />", {
      //                    text: profile.id
      //                  })).append($("<td />", {
      //                    html: $("<a />", {
      //                      role: "button",
      //                      "class": "btn btn-primary",
      //                      text: "Go!",
      //                      href: sky.REQUEST_CONTEXT_PATH + "/methodLogs/" + profile.id
      //                    })
      //                  })).appendTo($table.find('tbody'));
      //                });
      //
      //                $profiles.html($table);
      //              }, 100);
      //            }
      //          });
      //        });
      //      }(jQuery, sky, _));
    </script>
    <script>
      $(function () {
        google.setOnLoadCallback(function () {
          var $this = $("div#profile"),
              workId = $this.data('work-id');

          setInterval(function () {

            $.ajax({
              url: sky.REQUEST_CONTEXT_PATH + "/api/work/" + workId + "/profile",
              type: "GET"
            }).done(function (profile) {
              console.debug(profile);
              var methodLogs = profile.methodLogs;
              console.debug(methodLogs);

              var data = _(methodLogs).sortBy(function (methodLog) {
                return methodLog.startTime;
              }).pluck(function (methodLog, index) {
                return [new Date(methodLog.startTime), methodLog.elapsedTime];
              }).value();

              data.unshift(["Time", "ElapsedTime"]);

              var chart = new google.visualization.ScatterChart($this.find('#scatter')[0]);
              chart.draw(google.visualization.arrayToDataTable(data), {
                title: 'Age vs. Weight comparison',
                hAxis: {title: 'Time', gridlines: {
                  count: 5
                }},
                vAxis: {title: 'Elapsed Time'}
              });
            }).error(function (status, text) {
              console.error(text);
              console.error(status);
            });
          }, 100);
        });
      });
    </script>
  </tiles:putAttribute>
</tiles:insertTemplate>