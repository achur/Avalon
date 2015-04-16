package com.achur.avalon.storage;

import com.achur.avalon.entity.Game;

import com.google.common.base.Function;

import java.util.HashMap;

/**
 * Actual GameStore implementation.
 */
public class GameStoreImpl implements GameStore {

  private HashMap<Long, Game> gameStore = new HashMap<Long, Game>();

  /**
   * {@inheritDoc}
   */
  public Game getGame(Long id) {
    return gameStore.get(id);
  }

  /**
   * {@inheritDoc}
   */
  public Game saveGame(Game game) {
    gameStore.put(game.getId(), game);
    return game;
  }

  /**
   * {@inheritDoc}
   */
  public Game modifyGame(Long id, Function<Game, Game> modifier) {
    Game game = getGame(id);
    game = modifier.apply(game);
    return saveGame(game);
  }
}
