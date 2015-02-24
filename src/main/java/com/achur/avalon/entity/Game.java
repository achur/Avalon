package com.achur.avalon.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import lombok.Data;

/**
 * Represents a single game of Avalon.
 */
@Entity
@Data
public class Game {
  /**
   * Synthetic ID (woo boilerplate)
   */
  @Id
  Long id;

  /**
   * The name of the game
   */
  String name;
}
