/**
 * Indexpage controller.
 */

goog.provide('avalon.indexpage.IndexCtrl');

/**
 * Main page controller.
 *
 * @param {angular.$location} $location The Angular location service.
 * @constructor
 * @ngInject
 */
avalon.indexpage.IndexCtrl = function($location) {

  /**
   * @private{angular.$location}
   */
  this.location_ = $location;
};
