/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.pages.case', ['BlurAdmin.pages.case.list'])
         .config(routeConfig);

  /** @ngInject */
  function routeConfig($stateProvider) {
    $stateProvider
        .state('case', {
          url: '/case',
          templateUrl: 'app/pages/case/case.html',
          title: '用例',
          controller:'CaseController',
          sidebarMeta: {
            icon: 'ion-stats-bars',
            order: 990,
          },
        });
  }

})();
