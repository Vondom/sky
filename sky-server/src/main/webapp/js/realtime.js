(function ($, _, sky) {
  $(function () {
    if (!_.isEmpty($("div#method-logs"))) {
      var $this = $("div#method-logs"),
          profileId = $this.data('profile-id');
      $.ajax({
        url: sky.REQUEST_CONTEXT_PATH + "/api/methodLog/byProfile/"+profileId,
        type: "GET",
        success: function (methodLogs) {

        }
      });
    }
  });
}(jQuery, _, sky));