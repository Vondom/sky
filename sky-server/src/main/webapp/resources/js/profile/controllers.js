/**
 * Created by user1 on 2014-11-08.
 */
sky.app.controller("ProfileCtrl", function ($scope, $http, $element, $sce) {
  var $profile = $($element),
      workId = $profile.data("work-id"),

      groupByThreadName = function (methodLogs) {
        var result = {};

        _.forEach(methodLogs, function (methodLog) {
          if (_.isUndefined(result[methodLog.threadName])) {
            result[methodLog.threadName] = [];
          }
          result[methodLog.threadName].push(methodLog);
        });

        return result;
      };

  $http.get(sky.API_WORK_URL + "/" + workId + "/methodLogs")
      .success(function (methodLogs) {
        $scope.methodLogs = methodLogs;
        console.debug(methodLogs);

        var groupedMethodLogs = groupByThreadName(methodLogs);

        _.forEach(groupedMethodLogs, function (methodLogs, threadName) {
          var $chart = $("<span />").appendTo($profile.find('.chart'));
              chart = new google.visualization.PieChart($chart[0]),
              data = new google.visualization.DataTable({
                cols: [{id: 'name', label: 'Name', type: 'string'},
                  {id: 'elapsedTime', label: 'Elapsed Time', type: 'number'}],
                rows: [
                ]
              });

          _.forEach(methodLogs, function (methodLog) {
            var name = methodLog.methodKey.classKey.packageName + "." + methodLog.methodKey.classKey.name + "." + methodLog.methodKey.name + methodLog.methodKey.signature;
            data.addRow(
                [{v: name}, {v: methodLog.elapsedTime}]
            );
          });

          chart.draw(data, {
            title: threadName
          });
        });

      });

  $scope.getNameWithTabs = function (methodLog) {
    var result = "";
    for (var i = 0; i < methodLog.ordering; ++i)
      result = result + "&nbsp;&nbsp;";

    return $sce.trustAsHtml(result + methodLog.methodKey.classKey.packageName + "." + methodLog.methodKey.classKey.name + "." + methodLog.methodKey.name);
  }
});
