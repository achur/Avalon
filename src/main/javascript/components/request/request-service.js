goog.provide('avalon.request.RequestService');

goog.require('goog.object');

goog.scope(function() {

var API_PREFIX = '/_ah/api/avalon/v1/';

/**
 * Service for making Avalon API requests.
 * @param {angular.$http} $http The HTTP service.
 * @ngInject
 */
avalon.request.RequestService = function($http) {

  /**
   * @private {angular.$http}
   */
  this.http_ = $http;
};
var RequestService = avalon.request.RequestService;

/**
 * Perform a GET request.
 * @param {string} url The URL to hit.
 * @param {Object} params The URL params.
 * @return {angular.$q.Promise} A promise resolving the response.
 */
RequestService.prototype.get = function(url, params) {
  var queryStr = '';
  goog.object.forEach(params, function(value, param) {
    queryStr += '&' + param + '=' + value;
  });
  var actualUrl = API_PREFIX + url + '?' + queryStr.substr(1);
  return this.http_.get(actualUrl);
};

/**
 * Perform a POST request.
 * @param {string} url The URL to hit.
 * @param {Object} params The URL params.
 * @return {angular.$q.Promise} A promise resolving the response.
 */
RequestService.prototype.post = function(url, params) {
  var actualUrl = API_PREFIX + url;
  return this.http_.post(actualUrl, params);
};


});  // goog.scope
