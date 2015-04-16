package com.achur.avalon.storage;

import com.achur.avalon.entity.Player;

import java.util.HashMap;

/**
 * Actual PlayerStore implementation.
 */
public class PlayerStoreImpl implements PlayerStore {

  private HashMap<Long, Player> playerStore = new HashMap<Long, Player>();
  private long uid = 0;

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
    if (player.getId() == null) {
      player.setId(uid++);
    }
    playerStore.put(player.getId(), player);
    return player;
  }
}
