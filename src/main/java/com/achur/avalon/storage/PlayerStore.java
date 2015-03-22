package com.achur.avalon.storage;

import com.achur.avalon.entity.Player;

/**
 * Handles storing and updating {@link Player} objects.
 */
public interface PlayerStore {

  /**
   * Gets a player from the datastore.
   *
   * @param id The ID of the player to fetch.
   * @return The player that was fetched.
   */
  public Player getPlayer(Long id);

  /**
   * Saves a player to the datastore.
   *
   * @param player The player to persist.
   * @return The player that was persisted.
   */
  public Player savePlayer(Player player);
}
