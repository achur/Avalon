package com.achur.avalon.api;

import com.achur.avalon.entity.Game;

import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

import java.util.List;

/**
 * Defines the API for game-level actions.
 */
public class GameApi extends AvalonApi {

  @ApiMethod(name="game.join", path="game/join")
  public List<Game> listGames(@Named("id") Long id) {
    System.out.println("JOINING GAME " + id);
    return ImmutableList.<Game>of();
  }

}
