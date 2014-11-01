<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:insertTemplate template="/WEB-INF/include/layout.jsp">
  <tiles:putAttribute name="body">
    <div id="projects" class="container-fluid" data-user-id="${userId}" ng-controller="ProjectCtrl">
      <div class="row pull-right">
        <button data-toggle="modal" data-target="#create-project.modal" class="btn btn-md btn-success">
          Create
        </button>
        <button ng-click="importGithub(projects)" class="btn btn-md btn-default">
          Import from Github
        </button>
      </div>
      <section class="row">
        <div class="col-md-12">
          <div class="row">
            <h5 class="col-md-4">Id</h5>
            <h5 class="col-md-4">Name</h5>
            <h5 class="col-md-4">Actions</h5>
          </div>
          <div class="row" ng-repeat="project in projects">
            <div class="col-md-12">
              <div class="row">
                <span class="col-md-4">{{project.id}}</span>
                <span class="col-md-4">{{project.name}}</span>
                <span class="col-md-4">
                  <button id="project-{{project.id}}-open" class="btn btn-xs btn-info" ng-click="openProject(project)"><span class="fa fa-chevron-down"></span></button>
                </span>
              </div>
              <div id="details-{{project.id}}" class="row hidden">
                <div class="details col-md-12">
                  <div class="row" ng-repeat="executionUnit in project.executionUnits">
                    <div class="col-md-12">
                      <div class="row">
                        <span class="col-md-3">{{executionUnit.id}}</span>
                        <span class="col-md-3">{{executionUnit.jarFileName}}</span>
                        <span class="col-md-4">{{executionUnit.arguments}}</span>
                        <span class="col-md-2">
                          <button class="btn btn-default" ng-click="profileExecutionUnit(executionUnit)">
                            Go!
                          </button>
                          <button class="btn btn-info" ng-click="openExecutionUnit(executionUnit)">
                            <span id="open-execution-unit-{{executionUnit.id}}" class="fa fa-chevron-down"></span>
                          </button>
                          <button class="btn btn-danger" ng-click="deleteExecutionUnit(executionUnit)"><span class="fa fa-times"></span></button>
                        </span>
                      </div>
                      <div id="works-{{executionUnit.id}}" class="hidden">
                        <div class="row">
                          <h5 class="col-md-5">Id</h5>
                          <h5 class="col-md-5">Order</h5>
                          <h5 class="col-md-2"></h5>
                        </div>
                        <div class="row" ng-repeat="work in executionUnit.works">
                          <span class="col-md-5">{{work.id}}</span>
                          <span class="col-md-5">{{work.ordering}}</span>
                          <span class="col-md-2">
                            <button class="btn btn-primary" ng-click="openWork(work)">Show</button>
                            <button class="btn btn-danger" ng-click="deleteWork(work)"><span class="fa fa-times"></span></button>
                          </span>
                        </div>
                      </div>
                    </div>
                  </div>
                  <form role="form" class="row">
                    <div class="form-group col-md-3">
                      <label class="sr-only" for="inputId">Id</label>
                      <input id="inputId" type="text" class="form-control" placeholder="Id" disabled>
                    </div>
                    <div class="form-group col-md-4">
                      <label class="sr-only" for="inputJarFile">JarFile</label>
                      <input filename="project.tempExecutionUnit.jarFileName" fileread="project.tempExecutionUnit.jarFile" id="inputJarFile" type="file">
                    </div>
                    <div class="form-group col-md-4">
                      <label class="sr-only" for="inputArguments">Arguments</label>
                      <input ng-model="project.tempExecutionUnit.arguments" id="inputArguments" type="text" class="form-control" placeholder="Arguments">
                    </div>
                    <div class="col-md-1">
                      <button class="btn btn-primary" ng-click="addExecutionUnit(project)"><span class="fa fa-plus"></span></button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
      <div id="create-project" class="modal fade">
        <div class="modal-dialog">
          <form class="modal-content" ng-submit="createProject()">

            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
              <h4 class="modal-title">Create Project</h4>
            </div>
            <div class="modal-body">
              <div class="form-group">
                <label for="inputName">Name</label>
                <input ng-model="tempProject.name" name="name" type="text" class="form-control" id="inputName" placeholder="Name">
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
              <button type="submit" class="btn btn-primary">Create</button>
            </div>

          </form><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
      </div><!-- /.modal -->
    </div>
    <script>
      $(function () {
        $("#create-project.modal").on('hidden.bs.modal', function () {
          $(this).find('input').val('');
        });
      });
    </script>
  </tiles:putAttribute>
</tiles:insertTemplate>