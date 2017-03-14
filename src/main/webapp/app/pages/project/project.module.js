/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.pages.project', [])
         .config(routeConfig);

  /** @ngInject */
  function routeConfig($stateProvider) {
    $stateProvider
        .state('project', {
          url: '/project',
          templateUrl: 'app/pages/project/project.html',
          title: '测试项目',
          sidebarMeta: {
            icon: 'ion-stats-bars',
            order: 880,
          },
        });
  }

})();
