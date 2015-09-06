package com.achur.avalon.processors;

import com.achur.avalon.entity.Game;
import com.achur.avalon.entity.Player;

import java.util.List;

/**
 * Handles game-level logic for Avalon.
 */
public interface GameProcessor {

  /**
   * Fetches a game by ID and returns it. Note that this object should be used
   * for read-only purposes.
   *
   * @param id The ID of the game.
   * @return The game object with the given ID.
   */
  public Game getGame(Long id);

  /**
   * Gets the list of players in a given game.
   *
   * @param id The ID of the game.
   * @return The list of players in the game.
   */
  public List<Player> getPlayerList(Long id);

  /**
   * Gets the user ID of the player with the given email address in the given game.
   * Note that this object should be used for read-only purposes.
   *
   * @param id The ID of the game.
   * @param email The email of the user to lookup.
   * @return The player object of the user with the given email or null if such a user does'nt exist.
   */
  public Player getPlayer(Long id, String email);

  /**
   * Moves the game from the wait state to the start state.
   *
   * @param id The ID of the game to start.
   * @return The game object in its post-start state.
   */
  public Game startGame(Long id);

  /**
   * Moves the game from the start state to the team selection state.
   *
   * @param id The ID of the game to start.
   * @return The game object in its post-start state.
   */
  public Game startSelection(Long id);

  /**
   * Sets the current proposed team and moves into the team voting state.
   *
   * @param gameId The ID of the game.
   * @param proposerId The ID of the proposing player.
   * @param playerIdList A list of IDs of the proposed players.
   * @return The game object in its post-proposal state.
   */
  public Game proposeTeam(Long gameId, Long proposerId, List<Long> playerIdList);

  /**
   * Issues a vote on the current proposed team. If this is the last needed vote, moves to the quest
   * state.
   *
   * @param gameId The ID of the game.
   * @param voterId The ID of the voting player.
   * @param approve Whether the player approves the proposed team.
   * @return The game object in its post-vote state.
   */
  public Game issueTeamVote(Long gameId, Long voterId, Boolean approve);

  /**
   * Issues a vote on the current quest. If this is the last needed vote, moves to either the
   * team selection or the guess Merlin state.
   *
   * @param gameId The ID of the game.
   * @param voterId The ID of the voting player.
   * @param succeed Whether the player votes to succeed the quest.
   * @return The game object in its post-vote state.
   */
  public Game issueQuestVote(Long gameId, Long voterId, Boolean succeed);

  /**
   * Issues a guess in endgame of who Merlin is. Afterward moves game to end state.
   *
   * @param gameId The ID of the game.
   * @param voterId The ID of the voting player.
   * @param guessId The ID of the player guessed as Merlin.
   * @return The game object in its end state.
   */
  public Game guessMerlin(Long gameId, Long voterId, Long guessId);
}
