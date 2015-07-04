/**
 * Primary module for the Avalon app.
 */

goog.provide('avalon.application.module');
goog.provide('avalon.application.routeProvider');

goog.require('avalon.indexpage.IndexCtrl');

/**
 * @param {!angular.$routeProvider}
 * @ngInject
 */
avalon.application.routeProvider = function($routeProvider) {
  $routeProvider.
      when('/index', {
        templateUrl: 'static/templates/index.html',
        controller: 'IndexCtrl',
        controllerAs: 'indexCtrl'
      })
      .otherwise({redirectTo: '/index'});
};

/**
 * Main module
 * @type {angular.Module}
 */
avalon.application.module = angular.module('avalon.application', [
  'ngRoute',
  'ngSanitize'
]);

avalon.application.module.config(avalon.application.routeProvider);

avalon.application.module.run(['$rootScope', function($rootScope) {
  $rootScope['CONFIG'] = {};
}]);

avalon.application.module.controller('IndexCtrl', avalon.indexpage.IndexCtrl);
