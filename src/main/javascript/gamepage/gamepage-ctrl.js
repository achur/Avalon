/**
 * Game page controller.
 */

goog.provide('avalon.gamepage.GameCtrl');

/**
 * Create page controller.
 *
 * @param {angular.$routeParams} $routeParams The Angular route params service.
 * @constructor
 * @ngInject
 */
avalon.gamepage.GameCtrl = function($routeParams) {

  /**
   * @private{number}
   */
  this.gameId_ = $routeParams['gameId'];

  console.log("Entering game " + this.gameId_);
};
