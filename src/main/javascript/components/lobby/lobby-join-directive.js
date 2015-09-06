goog.provide('avalon.lobby.LobbyJoinDirective');

/**
 * The directive for a join button.
 * @ngInject
 */
avalon.lobby.LobbyJoinDirective = function(lobbyService) {
  return {
    scope: {
      id: '='
    },
    template: '<md-button data-ng-click="join()">Join Game</md-button>',
    link: function(scope) {
      scope.join = function() {
        lobbyService.join(scope.id);
      };
    }
  };
};
