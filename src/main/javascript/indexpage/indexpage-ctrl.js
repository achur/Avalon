/**
 * Indexpage controller.
 */

goog.provide('avalon.indexpage.IndexCtrl');

goog.scope(function() {

/**
 * Main page controller.
 *
 * @param {angular.lobby.LobbyService} lobbyService
 * @param {!Object} $mdDialog
 * @param {angular.Scope} $scope
 * @param {angular.Location} $location
 * @constructor
 * @ngInject
 */
avalon.indexpage.IndexCtrl =
    function(lobbyService, $mdDialog, $scope, $location) {

  /**
   * @const
   * @private {avalon.lobby.LobbyService}
   */
  this.lobbyService_ = lobbyService;

  /**
   * @const
   * @private {!Object}
   */
  this.mdDialog_ = $mdDialog;

  /**
   * @const
   * @private {angular.Scope}
   */
  this.scope_ = $scope;

  /**
   * @const
   * @private {angular.Location}
   */
  this.location_ = $location;

  /**
   * @type {!Array<!Object>}
   * @export
   */
  this.gameList = [];

  /**
   * @type {boolean}
   * @export
   */
  this.displayCreate = false;

  /**
   * @type {boolean}
   * @export
   */
  this.mordred = false;

  /**
   * @type {boolean}
   * @export
   */
  this.morgana = false;

  /**
   * @type {boolean}
   * @export
   */
  this.percival = false;

  this.loadLobby();
};
var IndexCtrl = avalon.indexpage.IndexCtrl;

/**
 * @export
 */
IndexCtrl.prototype.loadLobby = function() {
  this.lobbyService_.list().then(
    goog.bind(function(response) {
      this.gameList = response.data['items'];
    }, this), goog.bind(function(error) {
      console.log('Failed to load lobby');
    }, this));
};

/**
 * @export
 */
IndexCtrl.prototype.displayCreateDialog = function() {
  this.mordred = false;
  this.morgana = false;
  this.percival = false;
  this.displayCreate = true;
  this.mdDialog_.show({
    templateUrl: 'static/templates/createpage.html',
    scope: this.scope_.$new()
  });
};

/**
 * @export
 */
IndexCtrl.prototype.hideCreateDialog = function() {
  this.mdDialog_.cancel();
};

/**
 * @export
 */
IndexCtrl.prototype.createGame = function() {
  this.mdDialog_.cancel();
  this.lobbyService_.create(this.mordred, this.morgana, this.percival).then(
    goog.bind(function(response) {
      this.displayCreate = false;
      this.loadLobby();
    }, this), goog.bind(function(error) {
      console.log('Error creating game');
    }, this));
};

/**
 * @export
 */
IndexCtrl.prototype.openGame = function(id) {
  this.location_.path('/game/' + id + '/');
};

});  // goog.scope
