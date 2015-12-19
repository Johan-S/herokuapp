
app.controller("testform", function($scope) {
  $scope.change = function(e){
    console.log(e);
  }
});

app.config(function($routeProvider) {
  $routeProvider.when("/testform", {
    templateUrl: "/page/testform/testform.html",
    controller: "testform"
  })
});