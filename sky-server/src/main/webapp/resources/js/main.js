_.templateSettings.interpolate = /{{([\s\S]+?)}}/g;
$.ajaxSetup({
  dataType: 'json',
  contentType: 'application/json',
  processData: false
});

var sky = (function () {
  var contextPath = $("html").data('context-path'),
      accessToken = $("html").data('access-token');;

  return {
    app: angular.module("skyApp", []),

    // Constant Variables
    ACCESS_TOKEN: accessToken,
    REQUEST_CONTEXT_PATH: contextPath,
    API_EXECUTION_UNIT_URL: contextPath + "/api/execution-unit",
    API_PROJECT_URL: contextPath + "/api/project",
    API_USER_URL: contextPath + "/api/user",
    API_WORK_URL: contextPath + "/api/work",
    API_WORKER_URL: contextPath + "/api/worker",
    API_PROFILE_URL: contextPath + "/api/profile"
  }
}());

sky.app.directive("fileread", [function () {
  return {
    scope: {
      fileread: "="
    },
    link: function (scope, element, attributes) {
      element.bind("change", function (changeEvent) {
        var reader = new FileReader();
        reader.onload = function (loadEvent) {
          scope.$apply(function () {
            var targetResult = loadEvent.target.result;
            scope.fileread = {
              name: changeEvent.target.files[0].name,
              data: targetResult.split(',')[1]
            };
//            scope.fileread = loadEvent.target.result;
          });
        };
        reader.readAsDataURL(changeEvent.target.files[0]);
      });
    }
  }
}]);

$(function () {
  $(".switch").bootstrapSwitch();
});