/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.pages.suite', [])
         .config(routeConfig);

  /** @ngInject */
  function routeConfig($stateProvider) {
    $stateProvider
        .state('suite', {
          url: '/suite',
          templateUrl: 'app/pages/suite/suite.html',
          title: '测试集',
          sidebarMeta: {
            icon: 'ion-stats-bars',
            order: 890,
          },
        });
  }

})();
