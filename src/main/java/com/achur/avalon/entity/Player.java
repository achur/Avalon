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
   * The name of the player.
   */
  String name;

  /**
   * The role assigned to the player.
   */
  Role role;
}
