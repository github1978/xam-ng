(function(){

    angular
        .module('BlurAdmin.pages.case.ctrl',[])
        .controller('CaseController',['$scope','caseServices','initData',CaseController]);

    function CaseController($scope,caseServices,initData){
        $scope.caseData = [];
        $scope.caseDataPages = 10;
        $scope.queryCases = function(){
            caseServices.getCaseList().then(function(res){
                $scope.caseData = res;
            });
        };
    }

})();