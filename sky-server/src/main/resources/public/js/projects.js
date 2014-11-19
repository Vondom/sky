(function () {
  $(function () {
    var $projects = $("#projects");
    if (!_.isEmpty($projects)) {
      var uesrId = $projects.data('user-id');
      $.ajax({
        url: "/user/"+userId+"/projects",
        type: "GET",
        success: function (projects) {

        }
      })
    }
  });
});