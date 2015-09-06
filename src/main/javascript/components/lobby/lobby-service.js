goog.provide('avalon.lobby.LobbyService');

/**
 * Service for interacting with the lobby.
 * @param {!avalon.request.RequestService} requestService
 * @constructor
 * @ngInject
 */
avalon.lobby.LobbyService = function(requestService) {

  /**
   * @private {!avalon.request.RequestService}
   */
  this.requestService_ = requestService;
};

/**
 * Fetch the list of open games.
 * @return {angular.$q.Promise}
 */
avalon.lobby.LobbyService.prototype.list = function() {
  return this.requestService_.get('lobby/list', {});
};

/**
 * Create a new game.
 * @param {boolean} mordred Whether to include Mordred.
 * @param {boolean} morgana Whether to include Morgana.
 * @param {boolean} percival Whether to include Percival.
 * @return {angular.$q.Promise}
 */
avalon.lobby.LobbyService.prototype.create =
    function(mordred, morgana, percival) {
  return this.requestService_.post('lobby/create', {
    'mordred': mordred,
    'morgana': morgana,
    'percival': percival
  });
};

/**
 * Join a game.
 * @return {angular.$q.Promise}
 */
avalon.lobby.LobbyService.prototype.join = function(id) {
  return this.requestService_.post('lobby/join', {
    id: id
  });
};
