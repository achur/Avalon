package com.achur.avalon.api;

import com.achur.avalon.entity.Game;
import com.achur.avalon.entity.Player;
import com.achur.avalon.processors.LobbyProcessor;

import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
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
  public List<Game> listGames(@Named("param") String param) {
    System.out.println(param);
    return ImmutableList.<Game>of();
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
    System.out.println(includePercival ? "PERCIVAL" : "NO PERCIVAL");
    System.out.println(includeMordred ? "MORDRED" : "NO MORDRED");
    System.out.println(includeMorgana ? "MORGANA" : "NO MORGANA");
    return null;
  }

}
