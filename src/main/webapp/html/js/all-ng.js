angular.module('myApp', []).controller('userCtrl', function($scope,$http) {
    $('.list-group').sortable();
    $http.get("/xamng/case/getSlaves").then(function (response) {
        $scope.slaves = response.data.results;
    });

    $scope.excuteCase = function(){
        $http.post(
            "/xamng/case/excute",
            {
                slave:$scope.slave,
                browser:$scope.browser
            },
            {
                headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                transformRequest: function(data){return $.param(data);}
            }
        ).success(function(data, status, headers, config){

        });
    }
});