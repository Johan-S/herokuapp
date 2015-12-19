

app.controller("images", function($scope, $http) {
  $scope.images = [];
  $scope.index = 0;
  $http({
    url: "/imagelist",
    method: "GET"
  }).then(function(res) {
    $scope.images = res.data;
  });
});

app.config(function($routeProvider){
  $routeProvider.when("/images", {
    templateUrl: "/page/images/images.html",
    controller: "images"
  });
});