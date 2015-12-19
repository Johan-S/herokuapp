app.controller('source', function($scope, $http) {
  $scope.fileList = [];
  $scope.sourcetext = "";
  $scope.imgurl = "";
  $scope.type = "";
  $http({
    method: "GET", 
    url: "/files"
  }).then(function(res) {
    $scope.fileList = res.data;
  });
  
  $scope.getSource = function(url) {
    $http({
      method: "GET",
      url: url
    }).then(function(res) {
      var type = res.headers("Content-Type");
      if(type && type.match(/^image/)) {
        $scope.imgurl = "/" + url;
        $scope.type = "img";
      } else {
        $scope.sourcetext = res.data;
        $scope.type = "text";
      }
    });
  }
});

app.config(function($routeProvider) {
  $routeProvider.when('/source', {
    templateUrl: 'page/source/source.html',
    controller: 'source'
  });
});