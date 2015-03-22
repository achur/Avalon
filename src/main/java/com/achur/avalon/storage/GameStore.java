package com.achur.avalon.storage;

import com.achur.avalon.entity.Game;

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
}
