(function(){

    angular
        .module('BlurAdmin.pages.case.list',[])
        .controller('CaseController',CaseController);

    function CaseController($scope,$http){
        $scope.caseData = [];
        $scope.caseDataPages = 10;
        $scope.queryCases = function(){
            $http.get(
                '/xamng/case/queryCases'
            ).then(
                function(response){
                    $scope.caseData = response.data.results;
                }
            );
        };
    }

})();