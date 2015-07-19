/**
 * Primary module for the Avalon app.
 */

goog.provide('avalon.application.module');
goog.provide('avalon.application.routeProvider');

goog.require('avalon.createpage.CreateCtrl');
goog.require('avalon.gamepage.GameCtrl');
goog.require('avalon.indexpage.IndexCtrl');

/**
 * @param {!angular.$routeProvider}
 * @ngInject
 */
avalon.application.routeProvider = function($routeProvider) {
  $routeProvider.
      when('/index/', {
        templateUrl: 'static/templates/indexpage.html',
        controller: 'IndexCtrl',
        controllerAs: 'indexCtrl'
      }).
      when('/create/', {
        templateUrl: 'static/templates/createpage.html',
        controller: 'CreateCtrl',
        controllerAs: 'createCtrl'
      }).
      when('/game/:gameId/', {
        templateUrl: 'static/templates/gamepage.html',
        controller: 'GameCtrl',
        controllerAs: 'gameCtrl'
      }).
      otherwise({redirectTo: '/index/'});
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
avalon.application.module.controller('GameCtrl', avalon.gamepage.GameCtrl);
avalon.application.module.controller('CreateCtrl', avalon.createpage.CreateCtrl);
