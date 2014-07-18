<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:insertTemplate template="/WEB-INF/include/layout.jsp">
  <tiles:putAttribute name="body">
    <script type="text/javascript">
      $(function () {
        google.setOnLoadCallback(function () {
          var data = google.visualization.arrayToDataTable([
            ['Age', 'Weight'],
            [ 8, 12],
            [ 4, 5.5],
            [ 11, 14],
            [ 4, 5],
            [ 3, 3.5],
            [ 6.5, 7]
          ]);

          var options = {
            title: 'Age vs. Weight comparison',
            hAxis: {title: 'Age', minValue: 0, maxValue: 15, gridlines: {
              count: 4
            }},
            vAxis: {title: 'Weight', minValue: 0, maxValue: 15},
            legend: 'none'
          };

          var chart = new google.visualization.ScatterChart(document.getElementById('chart_div'));
          chart.draw(data, options);

          var data = new google.visualization.DataTable();
          data.addColumn('string', 'Name');
          data.addColumn('number', 'Salary');
          data.addColumn('boolean', 'Full Time Employee');
          data.addRows([
            ['Mike',  {v: 10000, f: '$10,000'}, true],
            ['Jim',   {v:8000,   f: '$8,000'},  false],
            ['Alice', {v: 12500, f: '$12,500'}, true],
            ['Bob',   {v: 7000,  f: '$7,000'},  true]
          ]);

          var table = new google.visualization.Table(document.getElementById('table_div'));
          table.draw(data, {showRowNumber: false});
        });
      });
    </script>
    <div id="chart_div" style="width: 900px; height: 500px;"></div>
    <div id='table_div'></div>
  </tiles:putAttribute>
</tiles:insertTemplate>