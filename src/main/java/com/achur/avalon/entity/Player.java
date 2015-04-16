package com.achur.avalon.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import lombok.Data;

/**
 * Represents a single game of Avalon.
 */
@Entity
@Data
public class Player {

  public enum Role {
    MERLIN,
    SERVANT,
    PERCIVAL,
    ASSASSIN,
    MINION,
    MORDRED,
    MORGANA
  }

  /**
   * Synthetic ID (woo boilerplate)
   */
  @Id
  Long id;

  /**
   * The ID of the game this player is attached to.
   */
  Long gameId;

  /**
   * The name of the player.
   */
  String name;

  /**
   * The role assigned to the player.
   */
  Role role;

  /**
   * The player's email. This is pulled from auth.
   */
  String email;
}
