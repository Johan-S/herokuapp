app.controller('random', function($scope, $interval) {
  $scope.count = 5;
  $scope.console = console;
  $scope.$watch('console.log(1);count', function() {
    var count = $scope.count - 0;
    if (count < 0)
      $scope.count = 0;
    else if (count > 99)
      $scope.count = 99;
    else
      $scope.arr = new Array(count);
    
  });
});


app.config(function($routeProvider) {
  $routeProvider.when('/random', {
            templateUrl: 'page/random/random.html',
            controller: 'random',
  });
});