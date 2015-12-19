
app.controller("users", function ($scope, $http) {
  $http({method: "GET", url: "/users"})
    .then(
    function (res) {
      $scope.users = res.data;
    });
});



app.config(function ($routeProvider) {
  $routeProvider.when('/users', {
    templateUrl: 'page/users/users.html',
    controller: 'users'
  });
});