package com.achur.avalon.storage;

import com.achur.avalon.entity.Game;

import com.google.common.base.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Actual GameStore implementation.
 */
public class GameStoreImpl implements GameStore {

  private HashMap<Long, Game> gameStore = new HashMap<Long, Game>();
  private static long uid = 0;

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
    if (game.getId() == null) {
      game.setId(uid++);
    }
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

  /**
   * {@inheritDoc}
   */
  public List<Game> queryGames(Game.State state) {
    List<Game> games = new ArrayList<>();
    for (Game game : gameStore.values()) {
      if (game.getState() == state) {
        games.add(game);
      }
    }
    return games;
  }
}
