app.controller('login',  function($scope, account) {
  $scope.name = "";
  $scope.password = "";
  $scope.login = function(form) {
    if (form.$valid)
      account.login($scope.name, $scope.password);
  };
});



app.config(function($routeProvider) {
  $routeProvider.when('/login', {
            templateUrl: 'page/login/loginform.html',
            controller: 'login',
  });
});