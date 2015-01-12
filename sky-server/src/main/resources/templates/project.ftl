<#include "/include/header.ftl">
    <div id="projects" data-user-id="${id}" ng-controller="ProjectCtrl">
      <div class="container">
        <div class="row pull-right" style="padding-bottom: 10px;">
          <button data-toggle="modal" data-target="#import-github.modal" class="btn btn-md btn-default">
            Import from Github
          </button>
        </div>
        <section class="row">
          <div class="project-table col-md-12">
            <div class="header row">
                <#--<h5 class="col-md-4">Id</h5>-->
              <h2 class="col-md-2">Name</h2>
              <h2 class="col-md-8">Description</h2>
              <h2 class="col-md-2">Open/Delete</h2>
            </div>
            <div class="contents row" ng-repeat="project in projects">
              <div class="col-md-12">
                <div class="row">
                    <#--<span class="col-md-4">{{project.id}}</span>-->
                  <div class="col-md-2"><span class="toggle" ng-click="toggleProject(project)">{{project.name}}</span></div>
                  <div class="col-md-8">{{project.description}}</div>
                  <div class="col-md-2">
                    <#--<button id="project-{{project.id}}-open" class="btn btn-xs btn-info" ng-click="openProject(project)"><span class="fa fa-chevron-down"></span></button>-->
                    <button class="btn btn-danger" ng-click="deleteProject(projects, $index)"><span
                        class="fa fa-times"></span></button>
                  </div>
                </div>
                <div id="details-{{project.id}}" class="row hidden">
                  <div class="details col-md-12">
                    <form role="form" class="contents row">
                      <div class="form-group col-md-2">
                        <label class="sr-only" for="inputName">Id</label>
                        <input id="inputName" type="text" class="form-control" placeholder="Name" ng-model="project.tempExecutionUnit.name">
                      </div>
                      <div class="form-group col-md-3">
                        <label class="sr-only" for="inputJarFile">JarFile</label>
                        <button class="form-control btn btn-warning" onclick="javascript:$(this).parent().find('input').click()">{{project.tempExecutionUnit.jarFileName||"File Upload"}}</button>
                        <input filename="project.tempExecutionUnit.jarFileName"
                               fileread="project.tempExecutionUnit.jarFile" id="inputJarFile" type="file" class="hidden">
                      </div>
                      <div class="form-group col-md-2">
                        <label class="sr-only" for="inputMainClassName">Main Class Name</label>
                        <input ng-model="project.tempExecutionUnit.mainClassName" id="inputMainClassName" type="text"
                               class="form-control" placeholder="Main Class Name">
                      </div>
                      <div class="form-group col-md-3">
                        <label class="sr-only" for="inputArguments">Arguments</label>
                        <input ng-model="project.tempExecutionUnit.arguments" id="inputArguments" type="text"
                               class="form-control" placeholder="Arguments">
                      </div>
                      <div class="col-md-2">
                        <button class="btn btn-primary" ng-click="addExecutionUnit(project)"><span
                            class="fa fa-plus"></span></button>
                      </div>
                    </form>
                    <div class="row" ng-repeat="executionUnit in project.executionUnits">
                      <div class="col-md-12">
                        <div class="row contents">
                          <span class="col-md-2">{{executionUnit.name}}</span>
                          <span class="col-md-3">{{executionUnit.jarFileName}}</span>
                          <span class="col-md-2">{{executionUnit.mainClassName}}</span>
                          <span class="col-md-3">{{executionUnit.arguments}}</span>
                          <span class="col-md-2">
                            <button class="btn btn-default" ng-click="profileExecutionUnit(executionUnit)">
                              Go!
                            </button>
                            <button class="btn btn-info" ng-click="openExecutionUnit(executionUnit)">
                              <span id="open-execution-unit-{{executionUnit.id}}" class="fa fa-chevron-down"></span>
                            </button>
                            <button class="btn btn-danger" ng-click="deleteExecutionUnit(project, $index)"><span
                                class="fa fa-times"></span></button>
                          </span>
                        </div>
                        <div id="works-{{executionUnit.id}}" class="works hidden">
                          <div class="header row">
                              <#--<h5 class="col-md-5">Id</h5>-->
                            <h5 class="col-md-3">Start Time</h5>
                            <h5 class="col-md-3">Average Time(ns)</h5>
                            <h5 class="col-md-3">Most Long Time(ns)</h5>
                            <h5 class="col-md-3"></h5>
                          </div>
                          <div class="contents row" ng-repeat="work in executionUnit.works">
                              <#--<span class="col-md-5">{{work.id}}</span>-->
                            <div class="col-md-3">{{toDate(work.startTime)}}</div>
                            <div class="col-md-3">{{work.averageTime}} ns</div>
                            <div class="col-md-3">{{work.mostLongTime}} ns</div>
                            <div class="col-md-3">
                              <button class="btn btn-primary" ng-click="openWork(work)">Show</button>
                              <button class="btn btn-danger" ng-click="deleteWork(executionUnit, $index)"><span
                                  class="fa fa-times"></span></button>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>

                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>
        <aside id="import-github" class="modal fade" tabindex="-1" role="dialog">
          <div class="modal-dialog modal-lg">
            <form role="form" class="modal-content" ng-submit="importGithubProject($event, githubProjects)">

              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                    class="sr-only">Close</span></button>
                <h4 class="modal-title">Import from github projects</h4>
              </div>
              <div class="modal-body">
                <div ng-repeat="org in githubProjects">
                  <div class="row" style="border-bottom: 2px inset #2d6ca2;">
                    <div class="col-md-12">
                      <h1>{{org.name}}</h1>
                    </div>
                  </div>
                  <div class="row" ng-repeat="repo in org.repos">
                    <dl class="dl-horizontal col-md-10">
                      <dt>{{repo.name}}</dt>
                      <dd>{{repo.description}}</dd>
                    </dl>
                    <div class="col-md-2 pull-right">
                        <i class="fa fa-2x fa-toggle-off" ng-click="toggleGithubProject($event, repo)"></i>
                    </div>
                  </div>
                </div>
              </div>
              <div class="modal-footer">
                <button type="submit" class="btn btn-primary">Import</button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
              </div>

            </form>
            <!-- /.modal-content -->
          </div>
          <!-- /.modal-dialog -->
        </aside>
        <!-- /.modal -->
        <aside id="create-project" class="modal fade" tabindex="-1" role="dialog">
          <div class="modal-dialog">
            <form class="modal-content" ng-submit="createProject()">

              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                    class="sr-only">Close</span></button>
                <h4 class="modal-title">Create Project</h4>
              </div>
              <div class="modal-body">
                <div class="form-group">
                  <label for="inputName">Name</label>
                  <input ng-model="tempProject.name" name="name" type="text" class="form-control floating-label" id="inputName"
                         placeholder="Name">
                </div>
                <div class="form-group">
                  <label for="inputDescription">Description</label>
                  <input ng-model="tempProject.description" name="description" type="text" class="form-control floating-label" id="inputDescription"
                         placeholder="Description">
                </div>
              </div>
              <div class="modal-footer">
                <button type="submit" class="btn btn-primary">Create</button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
              </div>

            </form>
            <!-- /.modal-content -->
          </div>
          <!-- /.modal-dialog -->
        </aside>
        <!-- /.modal -->
      </div>
    </div>
    <script>
      $(function () {
        $("#create-project.modal").on('hidden.bs.modal', function () {
          $(this).find('input').val('');
        });
      });
    </script>
<#include "/include/footer.ftl">