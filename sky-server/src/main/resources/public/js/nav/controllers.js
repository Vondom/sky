sky.app.controller("NavSigninCtrl", function ($scope, $http, $element) {
  var accessToken = sky.ACCESS_TOKEN,
      signed = accessToken != "";

  if (signed) {
    $http.get(sky.API_USER_URL + "/" + sky.ME_ID)
        .success(function (user) {
          $scope.user = user;
        });
  }
  $scope.isSigned = signed;
});
