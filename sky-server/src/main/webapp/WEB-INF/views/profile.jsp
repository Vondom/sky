<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:insertTemplate template="/WEB-INF/include/layout.jsp">
  <tiles:putAttribute name="body">
    <div id="profile" data-profile-id="${profileId}" data-work-id="${workId}" class="container">
      <div class="row">
        <div id="scatter"></div>
      </div>
      <div class="row" id="table">

      </div>
    </div>
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
              var methodLogs = profile.methodLogs,
                  threadNames = [],
                  data = _(methodLogs).sortBy(function (methodLog) {
                    return methodLog.startTime;
                  }).pluck(function (methodLog, index) {
                    var index = threadNames.indexOf(methodLog.threadName),
                        result = [];
                    if (index < 0) {
                      index = threadNames.length;
                      threadNames.push(methodLog.threadName);
                    }

                    result.push(new Date(methodLog.startTime));
                    for (var i = 0; i < index; ++i) result.push(null);
                    result.push(methodLog.elapsedTime);

                    return result;
                  }).value();

              data.unshift(_.flatten(["Time", threadNames]));

              var chart = new google.visualization.ScatterChart($this.find('#scatter')[0]);
              chart.draw(google.visualization.arrayToDataTable(data), {
                title: 'Work Results',
                hAxis: {title: 'Time', gridlines: {
                  count: 5
                }},
                vAxis: {title: 'Elapsed Time'}
              });

              var $target = $("#profile").find('#table').html(''),
                  $table = $("<table />", {
                    "class": "table"
                  }).append(
                      $("<tr/>").append($("<th/>", {
                        text: "#"
                      })).append($("<th/>", {
                        text: "Action"
                      }))
                  ).appendTo($target);

              _.forEach(profile.methodLogs, function (methodLog) {

                $("<tr />").append($("<td />", {
                  text: methodLog.id
                })).append($("<td />", {
                  html: $("<a />", {
                    role: "button",
                    "class": "btn btn-primary",
                    text: "Go!",
                    href: sky.REQUEST_CONTEXT_PATH + "/methodLogs/" + methodLog.id
                  })
                })).appendTo($table);
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