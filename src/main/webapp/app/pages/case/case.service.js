(function(){
    angular
        .module('BlurAdmin.pages.case.services',[])
        .factory("caseServices", ['$q','$http',function($q,$http){
                return {
                    getCaseList: function(){
                        var deferred = $q.defer();
                        var promise = deferred.promise;
                        $http.get('/xamng/case/queryCases')
                        .success(function(response){
                            deferred.resolve(response.results);
                        })
                        .error(function(error){
                            deferred.reject(error);
                        });
                        return promise;
                    }
                }
        }]);

})();