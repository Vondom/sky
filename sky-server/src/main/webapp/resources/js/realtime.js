(function ($, _, sky) {
  $(function () {
    google.setOnLoadCallback(function () {
      if (!_.isEmpty($("div#method-logs"))) {
        var $this = $("div#method-logs"),
            profileId = $this.data('profile-id');
        $.ajax({
          url: sky.REQUEST_CONTEXT_PATH + "/api/methodLog/byProfile/" + profileId,
          type: "GET",
          success: function (methodLogs) {
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
          }
        });
      }
    });
  });
}(jQuery, _, sky));