package com.achur.avalon.processors;

import com.achur.avalon.entity.Game;
import com.achur.avalon.entity.Player;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Set;

public class Constants {

  /**
   * Number of allowed votes before the game is over.
   */
  public static final int MAX_VOTES = 5;

  /**
   * The roles that are bad.
   */
  public static final Set<Player.Role> BAD_ROLES = ImmutableSet.of(
      Player.Role.ASSASSIN,
      Player.Role.MINION,
      Player.Role.MORGANA,
      Player.Role.MORDRED);

  /**
   * The minimum number of players necessary.
   */
  public static final Integer MIN_PLAYERS = 5;

  /**
   * The number of bad players for each total number of players, starting with
   * MIN_PLAYERS.
   */
  public static final List<Integer> BAD_PLAYER_COUNTS = ImmutableList.of(
      2, 2, 3, 3, 3, 4);

  /**
   * The number of quests in a game.
   */
  public static final Integer NUM_QUESTS = 5;

  /**
   * The maximum number of players.
   */
  public static final Integer MAX_PLAYERS = MIN_PLAYERS + BAD_PLAYER_COUNTS.size() - 1;

  /**
   * The number of players sent on each mission, for each number of players.
   * A negative value means two fails are required to fail the mission.
   */
  private static final List<ImmutableList<Integer>> NUM_MISSION_PLAYERS =
      ImmutableList.of(
          ImmutableList.of(2, 3, 2, 3 ,3),
          ImmutableList.of(2, 3, 3, 3, 4),
          ImmutableList.of(2, 3, 3, -4, 4),
          ImmutableList.of(3, 4, 4, -5, 5),
          ImmutableList.of(3, 4, 4, -5, 5),
          ImmutableList.of(3, 4, 4, -5, 5));

  /**
   * @param totalPlayers The number of players in the game.
   * @param currentQuest the number of the current quest (indexed from 0).
   * @return the number of players going on the current quest.
   */
  public static int getNumPlayers(int totalPlayers, int currentQuest) {
    return Math.abs(
        NUM_MISSION_PLAYERS.get(totalPlayers - MIN_PLAYERS).get(currentQuest));
  }

  /**
   * @param totalPlayers The number of players in the game.
   * @param currentQuest the number of the current quest (indexed from 0).
   * @return the number of fails required to fail the current quest.
   */
  public static int getNumFailsRequired(int totalPlayers, int currentQuest) {
    return NUM_MISSION_PLAYERS.get(totalPlayers - MIN_PLAYERS).get(currentQuest) > 0 ?
        1 : 2;
  }
}
