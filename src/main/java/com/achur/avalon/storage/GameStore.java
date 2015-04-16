package com.achur.avalon.storage;

import com.achur.avalon.entity.Game;

import com.google.common.base.Function;

import java.util.List;

/**
 * Handles storing and updating {@link Game} objects.
 */
public interface GameStore {

  /**
   * Gets the game from the datastore.
   *
   * @param id The ID of the game.
   * @return The game fetched from the datastore.
   */
  public Game getGame(Long id);

  /**
   * Persists the game to the datastore.
   *
   * @param game The game to persist.
   * @return The game that was persisted.
   */
  public Game saveGame(Game game);

  /**
   * Performs an atomic modification to a game.
   *
   * @param id The ID of the game to modify.
   * @param modifier A function that modifies the Game which will be
   *     executed atomically for each game.
   * @return The modified game.
   */
  public Game modifyGame(Long id, Function<Game, Game> modifier);

  /**
   * Returns all the games in the given state.
   *
   * @param state The state of the games to query.
   */
  public List<Game> queryGames(Game.State state);
}
