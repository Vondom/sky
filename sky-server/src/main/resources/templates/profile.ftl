<#include "/include/header.ftl">
    <div data-work-id="${id}" class="container" ng-controller="ProfileCtrl">
      <div class="chart row">

      </div>
      <div class="row">
        <table class="table table-striped">
          <tr>
            <th>Name</th>
            <th>Signature</th>
            <th>Elapsed Time</th>
          </tr>
          <tr ng-repeat="log in methodLogs">
            <td ng-bind-html="getNameWithTabs(log)">
            </td>
            <td>{{log.methodKey.signature}}</td>
            <td>{{log.elapsedTime}}ns</td>
          </tr>
        </table>
      </div>
    </div>
<#include "/include/footer.ftl">