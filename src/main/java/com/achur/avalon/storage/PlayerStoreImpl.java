package com.achur.avalon.storage;

import com.achur.avalon.entity.Player;

import java.util.HashMap;

/**
 * Actual PlayerStore implementation.
 */
public class PlayerStoreImpl implements PlayerStore {

  private HashMap<Long, Player> playerStore = new HashMap<Long, Player>();

  /**
   * {@inheritDoc}
   */
  public Player getPlayer(Long id) {
    return playerStore.get(id);
  }

  /**
   * {@inheritDoc}
   */
  public Player savePlayer(Player player) {
    playerStore.put(player.getId(), player);
    return player;
  }
}
