
app.controller("scb", function($scope, $http) {
  
  $scope.setsub = function(name) {
    $http({
      url: "http://api.scb.se/OV0104/v1/doris/sv/ssd/" + name,
      method: "GET"
    }).then(function(res) {
      $scope.subareas = res.data;
    });
  };
  
  $http({
    url: "http://api.scb.se/OV0104/v1/doris/sv/ssd/",
    method: "GET"
  }).then(function(res) {
    $scope.areas = res.data;
    $scope.setsub($scope.areas[0].id);
  });
});

app.config(function($routeProvider) {
  $routeProvider.when("/scb", {
    templateUrl: "page/scb/scb.html",
    controller: "scb"
  });
});