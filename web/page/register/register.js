app.controller('register', function($scope, account) {
  $scope.name = "";
  $scope.password = "";
  $scope.register = function(form) {
    if (form.$valid)
      account.register($scope.name, $scope.password);
  };
  
});


app.config(function($routeProvider) {
  $routeProvider.when('/register', {
            templateUrl: 'page/register/registerform.html',
            controller: 'register',
  });
});