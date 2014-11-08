sky.app.controller("ProjectCtrl", function ($scope, $http, $element) {
  var $projects = $element,
      userId = $projects.data('user-id'),
      generateFunkyName = sky.utils.haiku,
      removeDuplicate = function (projects, repos) {
        _.forEach(repos, function (repo, i) {
          if(_.find(projects, function (prj) { return prj.name == repo.name; })) {
            repos.slice(i, 1);
          }
        });
      };

  $http.get(sky.API_USER_URL + "/" + userId + "/projects")
      .success(function (data) {
        $scope.projects = data;

       if (!_.isUndefined($scope.githubProjects)) {
         _.forEach($scope.githubProjects, function (collection, i) {
           removeDuplicate(data, collection.repos);
         });
       }
      })
      .error(function (data, status) {
        console.error(status + ": " + data);
      });

  $http.get(sky.API_USER_URL + "/" + userId + "/github/repositories")
      .success(function (data) {
        $scope.githubProjects = [];
        _.forEach(data, function (repos, org) {
          if (!_.isUndefined($scope.projects)) {
            removeDuplicate($scope.projects, repos);
          }

          $scope.githubProjects.push({
            name: org,
            repos: repos
          });
        });
      })
      .error(function (message, status) {
        console.error(status + ": " + message);
      });

  $scope.tempProject = {
    name: "",
    description: ""
  };

  $scope.toDate = function (time) {
    return time ? new Date(time).toDateString() : "";
  };

  $scope.importGithubProject = function (event, githubProjects) {
    var $this = $(event.target),
        $modal = $this.parents(".modal");

    _.forEach(githubProjects, function (org) {
      _.forEach(org.repos, function (repo, i) {
        if (true === repo.checkToProject) {
          delete repo.checkToProject;
          $http.post(sky.API_PROJECT_URL + "/github", repo)
              .success(function (project) {
                console.debug(project);
                $scope.projects.push(project);

                org.repos.splice(i, 1);
                $modal.modal('hide');
              })
              .error(function (message) {
                console.error(message);
              });
        }
      });
    });
  };

  $scope.toggleGithubProject = function (event, repo) {
    var $this = $(event.currentTarget);

    if ($this.hasClass("fa-toggle-off")) {
      repo.checkToProject = true;
      $this.removeClass("fa-toggle-off").addClass("fa-toggle-on");
    } else if ($this.hasClass("fa-toggle-on")) {
      repo.checkToProject = false;
      $this.removeClass("fa-toggle-on").addClass("fa-toggle-off");
    } else {
      console.error($this.context + "must be to have class of 'fa-toggle-on' or 'fa-toggle-off'");
    }
  };

  $scope.toggleProject = function (project) {
    var $details = $projects.find("#details-" + project.id),
        $buttonIcon = $projects.find("#project-" + project.id + "-open > span.fa");

    project.tempExecutionUnit = {
      name: generateFunkyName()
    };

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

  $scope.deleteProject = function (projects, $index) {
    var project = projects[$index];

    $http.delete(sky.API_PROJECT_URL + "/" + project.id)
        .success(function () {
          projects.splice($index, 1);
        }).error(function (message) {
          console.error(message);
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
          $projects.find('#details-' + project.id + " form input").val('');

          project.tempExecutionUnit.name = generateFunkyName();
        })
        .error(function (text, status) {
          console.error(status + ": " + text);
//          console.debug(JSON.stringify(text));
        });
  };

  $scope.openExecutionUnit = function (executionUnit) {
    var $works = $projects.find("#works-" + executionUnit.id),
        $span = $("#open-execution-unit-" + executionUnit.id);
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

  $scope.deleteExecutionUnit = function (project, $index) {
    var executionUnit = project.executionUnits[$index];

    $http.delete([sky.API_EXECUTION_UNIT_URL, executionUnit.id].join('/'))
        .success(function () {
          project.executionUnits.splice($index, 1);
        })
        .error(function (text, status) {
          console.error(status + ": " + text);
        });
  };

  $scope.openWork = function (work) {
    window.location.href = sky.REQUEST_CONTEXT_PATH + "/profile/" + work.id;
  };

  $scope.deleteWork = function (executionUnit, $index) {
    var work = executionUnit.works[$index];
    $http.delete(sky.API_WORK_URL, work)
        .success(function () {
          executionUnit.works.splice($index, 1);
        })
        .error(function (text, status) {

        });
  }
});