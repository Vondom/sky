_.templateSettings.interpolate = /{{([\s\S]+?)}}/g;
$.ajaxSetup({
  dataType: 'json',
  contentType: 'application/json',
  processData: false
});

var sky = (function () {
  var contextPath = $("html").data('context-path'),
      accessToken = $("html").data('access-token'),
      userId = $("html").data("user-id");

  return {
    utils: {},
    app: angular.module("skyApp", ['googlechart']),

    // Constant Variables
    ACCESS_TOKEN: accessToken,
    REQUEST_CONTEXT_PATH: contextPath,
    ME_ID: userId,
    API_EXECUTION_UNIT_URL: contextPath + "/api/execution-unit",
    API_PROJECT_URL: contextPath + "/api/project",
    API_USER_URL: contextPath + "/api/user",
    API_WORK_URL: contextPath + "/api/work",
    API_WORKER_URL: contextPath + "/api/worker",
    API_PROFILE_URL: contextPath + "/api/profile"
  }
}());

//sky.app.run(function ($http) {
//  console.debug($http.defaults.headers);
//});

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

            element.parent().find('button').text(scope.fileread.name);
          });
        };
        reader.readAsDataURL(changeEvent.target.files[0]);
      });
    }
  }
}]);

sky.utils.haiku = function() {
  var adjs, nouns, rnd;
  adjs = ["autumn", "hidden", "bitter", "misty", "silent", "empty", "dry", "dark", "summer", "icy", "delicate", "quiet", "white", "cool", "spring", "winter", "patient", "twilight", "dawn", "crimson", "wispy", "weathered", "blue", "billowing", "broken", "cold", "damp", "falling", "frosty", "green", "long", "late", "lingering", "bold", "little", "morning", "muddy", "old", "red", "rough", "still", "small", "sparkling", "throbbing", "shy", "wandering", "withered", "wild", "black", "young", "holy", "solitary", "fragrant", "aged", "snowy", "proud", "floral", "restless", "divine", "polished", "ancient", "purple", "lively", "nameless"];
  nouns = ["waterfall", "river", "breeze", "moon", "rain", "wind", "sea", "morning", "snow", "lake", "sunset", "pine", "shadow", "leaf", "dawn", "glitter", "forest", "hill", "cloud", "meadow", "sun", "glade", "bird", "brook", "butterfly", "bush", "dew", "dust", "field", "fire", "flower", "firefly", "feather", "grass", "haze", "mountain", "night", "pond", "darkness", "snowflake", "silence", "sound", "sky", "shape", "surf", "thunder", "violet", "water", "wildflower", "wave", "water", "resonance", "sun", "wood", "dream", "cherry", "tree", "fog", "frost", "voice", "paper", "frog", "smoke", "star"];
  rnd = Math.floor(Math.random() * Math.pow(2, 12));
  return "" + adjs[rnd >> 6 % 64] + "-" + nouns[rnd % 64] + "-" + Math.floor(Math.random() * 10000);
};