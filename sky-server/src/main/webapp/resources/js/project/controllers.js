sky.app.controller("ProjectCtrl", function ($scope, $http) {
  var $projects = $("#projects"),
      userId = $projects.data('user-id');

  $http.get(sky.API_USER_URL + "/" + userId + "/projects")
      .success(function (data) {
        $scope.projects = data;
        console.debug(data);
      })
      .error(function (data, status) {
        console.error(status + ": " + data);
      });

  $scope.tempProject = {
    name: ""
  };

  $scope.importGithub = function (projects) {

  };

  $scope.openProject = function (project) {
    var $details = $projects.find("#details-" + project.id),
        $buttonIcon = $projects.find("#project-" + project.id + "-open > span.fa");

    if ($details.hasClass("hidden")) {
      $http.get(sky.API_PROJECT_URL + "/" + project.id + "/execution-units")
          .success(function (executionUnits) {
            project.executionUnits = executionUnits;

            $buttonIcon.removeClass("fa-chevron-down").addClass("fa-chevron-up");
            $details.toggleClass("hidden");
          })
          .error(function (text, status) {
            console.error(status + ": " + text);
          });
    } else {
      $buttonIcon.removeClass("fa-chevron-up").addClass("fa-chevron-down");
      $details.toggleClass("hidden");
    }
  };

  $scope.createProject = function () {
    $http.post(sky.API_PROJECT_URL, $scope.tempProject)
        .success(function (project) {
          $scope.projects.push(project);
          $projects.find('#create-project.modal').modal('hide');
        })
        .error(function (data, status) {
          console.error(status + ": " + data);
        });
  };

  $scope.profileExecutionUnit = function (executionUnit) {
    var work = {
      executionUnit: executionUnit
    };

    $http.post(sky.API_WORK_URL, work)
        .success(function (work) {
          console.debug("working success");
//          window.location.href = sky.REQUEST_CONTEXT_PATH + "/profile";
        })
        .error(function (message, status) {
          console.error("status: " + status + ", message: " + message);
        })
  };

  $scope.addExecutionUnit = function (project) {
    var inputExecutionUnit = project.tempExecutionUnit;

    if (_.isObject(inputExecutionUnit.jarFile)) {
      inputExecutionUnit.jarFileName = inputExecutionUnit.jarFile.name;
      inputExecutionUnit.jarFile = inputExecutionUnit.jarFile.data;
    }

    $http.put(sky.API_PROJECT_URL + "/" + project.id + "/execution-units", inputExecutionUnit)
        .success(function (executionUnit) {
          project.executionUnits.push(executionUnit);
          $projects.find('#details-'+project.id+" form input").val('');
        })
        .error(function (text, status) {
          console.error(status + ": " + text);
        });
  };

  $scope.openExecutionUnit = function (executionUnit) {
    var $works = $projects.find("#works-" + executionUnit.id),
        $span = $("#open-execution-unit-"+executionUnit.id);
    if (!$works.hasClass("hidden")) {
      $span.removeClass("fa-chevron-up").addClass("fa-chevron-down");
      $works.addClass("hidden");
    } else {
      $http.get(sky.API_EXECUTION_UNIT_URL + "/" + executionUnit.id + "/works")
          .success(function (works) {
            executionUnit.works = works;
            $span.removeClass("fa-chevron-down").addClass("fa-chevron-up");
            $works.removeClass("hidden");

//            console.debug(JSON.stringify(works));
          }).error(function (message, status) {
            console.error("status: " + status + ", message: " + message);
          });
    }
  };

  $scope.deleteExecutionUnit = function (executionUnit) {
    $http.delete([sky.API_EXECUTION_UNIT_URL, executionUnit.id].join('/'))
        .success(function () {
          delete executionUnit;
        })
        .error(function (text, status) {
          console.error(status + ": " + text);
        });
  };

  $scope.openWork = function (work) {
    window.location.href = sky.REQUEST_CONTEXT_PATH + "/profile/" + work.id;
  }

  $scope.deleteWork = function (work) {
//    $http.delete(sky.API_WORK_URL, work)
//        .success(function () {
//
//        })
//        .error(function text, status) {}
  }
});