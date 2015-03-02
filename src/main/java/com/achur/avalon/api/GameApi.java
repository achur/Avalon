package com.achur.avalon.api;

import com.achur.avalon.entity.Game;
import com.achur.avalon.processors.GameProcessor;

import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

import java.util.List;

/**
 * Defines the API for game-level actions.
 */
public class GameApi extends AvalonApi {

  /**
   * The Game Processor which handles the behind-the-scenes logic.
   */
  private GameProcessor gameProcessor;

  /**
   * Guice-injected constructor.
   */
  @Inject
  GameApi(GameProcessor gameProcessor) {
    this.gameProcessor = gameProcessor;
  }

  /**
   * TODO(achur): move this to LobbyApi.
   */
  @ApiMethod(name="game.join", path="game/join")
  public Game joinGame(@Named("id") Long id) {
    System.out.println("JOINING GAME " + id);
    return null;
  }

}
