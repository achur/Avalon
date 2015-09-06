package com.achur.avalon.api;

import com.achur.avalon.entity.Game;
import com.achur.avalon.entity.Player;
import com.achur.avalon.processors.LobbyProcessor;

import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.users.User;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

import java.util.List;
import javax.annotation.Nullable;

/**
 * Defines the API for lobby-level actions.
 */
public class LobbyApi extends AvalonApi {

  /**
   * The Lobby Processor which handles the behind-the-scenes logic.
   */
  private LobbyProcessor lobbyProcessor;

  /**
   * Guice-injected constructor.
   */
  @Inject
  LobbyApi(LobbyProcessor lobbyProcessor) {
    this.lobbyProcessor = lobbyProcessor;
  }

  /**
   * Lists the currently open games.
   */
  @ApiMethod(name="lobby.list", path="lobby/list")
  public List<Game> listGames() {
    return lobbyProcessor.listGames();
  }

  /**
   * Creates a game and returns the ID to it.
   */
  @ApiMethod(name="lobby.create", path="lobby/create")
  public Game createGame(
      @Nullable @Named("percival") Boolean includePercival,
      @Nullable @Named("mordred") Boolean includeMordred,
      @Nullable @Named("morgana") Boolean includeMorgana) {
    if (includePercival == null) {
      includePercival = false;
    }
    if (includeMordred == null) {
      includeMordred = false;
    }
    if (includeMorgana == null) {
      includeMorgana = false;
    }
    return lobbyProcessor.createGame(includePercival, includeMordred, includeMorgana);
  }

  /**
   * Joins a game as the logged-in user.
   */
  @ApiMethod(name="lobby.join", path="lobby/join")
  public Player joinGame(@Named("id") Long gameId, User user) {
    System.out.println(user);
    return lobbyProcessor.joinGame(gameId, user.getNickname(), user.getEmail());
  }

}
