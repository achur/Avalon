/**
 * Primary module for the Avalon app.
 */

goog.provide('avalon.application.module');
goog.provide('avalon.application.routeProvider');

goog.require('avalon.gamepage.GameCtrl');
goog.require('avalon.indexpage.IndexCtrl');
goog.require('avalon.lobby.module');
goog.require('avalon.request.module');

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
  avalon.lobby.module.name,
  avalon.request.module.name,
  'ngRoute',
  'ngSanitize',
  'ngMaterial'
]);

avalon.application.module.config(avalon.application.routeProvider);

avalon.application.module.controller('IndexCtrl', avalon.indexpage.IndexCtrl);
avalon.application.module.controller('GameCtrl', avalon.gamepage.GameCtrl);

window.init = function() {

  gapi.client.load('avalon', 'v1', null, window.config.ROOT).then(function() {
    console.log('loaded');
    return gapi.auth.authorize(window.config.AUTH);
  }).then(function() {
    avalon.application.module.run(['$rootScope', function($rootScope) {
      $rootScope['CONFIG'] = {};
    }]);
  });

};
