/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.pages.case', ['BlurAdmin.pages.case.ctrl','BlurAdmin.pages.case.services'])
         .config(routeConfig);

  /** @ngInject */
  function routeConfig($stateProvider) {
    $stateProvider
        .state('case', {
          url: '/case',
          templateUrl: 'app/pages/case/case.html',
          title: '用例',
          sidebarMeta: {
            icon: 'ion-stats-bars',
            order: 990,
          },
          controller:'CaseController',
          resolve:{
            initData:function($q,caseServices){
                var deferred = $q.defer();
                caseServices.getCaseList().then(function(res){
                    deferred.resolve(res);
                });
                return deferred.promise;
            }
          }
        });
  }

})();
