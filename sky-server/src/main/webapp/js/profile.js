(function ($, sky) {
  $(function () {
    if (!_.isEmpty($("#profiles"))) {
      $.ajax({
        url: sky.REQUEST_CONTEXT_PATH + "/api/profile",
        type: 'GET',
        success: function (profiles) {
//          console.debug(profiles);

          var $table = $("<table />", {
            "class": "table"
          }).append(
              $("<tr/>").append($("<th/>", {
                text: "#"
              })).append($("<th/>", {
                text: "Action"
              })).wrap("<thead></thead>")
          ).append("<tbody />").appendTo("#profiles");

          _.forEach(profiles, function (profile) {
            $("<tr />").append($("<td />", {
              text: profile.id
            })).append($("<td />", {
              html: $("<a />", {
                role: "button",
                "class": "btn btn-primary",
                text: "Go!",
                href: sky.REQUEST_CONTEXT_PATH + "/methodLogs/" + profile.id
              })
            })).appendTo($table.find('tbody'));
          });
        }
      });
    }
  });
}(jQuery, sky, _));