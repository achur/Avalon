package com.achur.avalon.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import lombok.Data;

import java.util.List;

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
   * The players in the game.
   */
  List<Long> players;

  /**
   * The roles in the game.
   */
  List<Player.Role> roles;

  public enum State {
    WAIT,
    START,
    TEAM_SELECTION,
    TEAM_VOTING,
    QUEST,
    GUESS_MERLIN,
    END
  }

  /**
   * The state of the game
   */
  State state;

  /**
   * The number of votes in the current iteration of the game.
   */
  Integer voteCount;

  /**
   * Results of the quests (true: success, false: fail).
   */
  List<Boolean> questResults;

  /**
   * The current team proposed.
   */
  List<Long> currentTeam;

  /**
   * The current leader.
   */
  Long currentLeader;

  /**
   * The current votes for the given team.
   */
  List<Long> teamVotesYay;

  /**
   * The current votes against the given team.
   */
  List<Long> teamVotesNay;

  /**
   * The current votes for the given quest.
   */
  List<Long> questVotesYay;

  /**
   * The current votes against the given quest.
   */
  List<Long> questVotesNay;

  /**
   * Outcome of the game (true = good wins).
   */
  Boolean outcome;
}
