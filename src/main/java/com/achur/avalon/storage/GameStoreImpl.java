package com.achur.avalon.storage;

import com.achur.avalon.entity.Game;

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

}
