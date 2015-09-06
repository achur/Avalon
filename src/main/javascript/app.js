/**
 * Primary module for the Avalon app.
 */

goog.provide('avalon.application.module');
goog.provide('avalon.application.routeProvider');
goog.provide('avalon.config');

goog.require('avalon.gamepage.GameCtrl');
goog.require('avalon.indexpage.IndexCtrl');
goog.require('avalon.lobby.module');
goog.require('avalon.request.module');
goog.require('avalon.topbar.module');

/**
 * Make global config available as an import.
 */
avalon.config = window.config;
avalon.config['user'] = {};

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
  avalon.topbar.module.name,
  'ngRoute',
  'ngSanitize',
  'ngMaterial'
]);

avalon.application.module.config(avalon.application.routeProvider);

avalon.application.module.controller('IndexCtrl', avalon.indexpage.IndexCtrl);
avalon.application.module.controller('GameCtrl', avalon.gamepage.GameCtrl);

var rootScope;
avalon.application.module.run(['$rootScope', function($rootScope) {
  rootScope = $rootScope;
  rootScope.config = avalon.config;
}]);

window.init = function() {
  gapi.client.load('avalon', 'v1', null, window.config.ROOT).then(function() {
    return gapi.auth.authorize(window.config.AUTH);
  }).then(function() {
    return gapi.client.load('oauth2', 'v2');
  })
  .then(function() {
    return gapi.client.oauth2.userinfo.get();
  }).then(function(response) {
    // Update config, then manually redraw.
    avalon.config['user'] = response.result;
    rootScope.$apply();
  });

};
