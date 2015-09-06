/**
 * Module for the Avalon lobby.
 */

goog.provide('avalon.lobby.module');

goog.require('avalon.lobby.LobbyJoinDirective');
goog.require('avalon.lobby.LobbyService');

avalon.lobby.module = angular.module('avalon.lobby', []).
    service('lobbyService', avalon.lobby.LobbyService).
    directive('lobbyJoin', avalon.lobby.LobbyJoinDirective);
