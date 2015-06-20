package com.achur.avalon.api;

import com.achur.avalon.entity.Game;
import com.achur.avalon.processors.GameProcessor;

import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.users.User;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

import java.util.List;

/**
 * Defines the API for game-level actions.
 */
public class GameApi extends AvalonApi {

  /**
   * The Game Processor which handles the behind-the-scenes logic.
   */
  private GameProcessor gameProcessor;

  /**
   * Guice-injected constructor.
   */
  @Inject
  GameApi(GameProcessor gameProcessor) {
    this.gameProcessor = gameProcessor;
  }

  private Long getUserId(Long gameId, User user) {
    return gameProcessor.getPlayer(gameId, user.getEmail()).getId();
  }

  /**
   * Gets the game by the given ID.
   */
  @ApiMethod(name="game.get", path="game/get")
  public Game getGame(@Named("id") Long gameId) {
    return gameProcessor.getGame(gameId);
  }

  /**
   * Starts the game with the given ID.
   */
  @ApiMethod(name="game.start", path="game/start")
  public Game startGame(@Named("id") Long gameId) {
    return gameProcessor.startGame(gameId);
  }

  /**
   * Takes the game from the start state to the team selection state.
   */
  @ApiMethod(name="game.select", path="game/select")
  public Game startSelection(@Named("id") Long gameId) {
    return gameProcessor.startSelection(gameId);
  }

  /**
   * Sets the current team being proposed.
   */
  @ApiMethod(name="game.proposeTeam", path="game/propose-team")
  public Game proposeTeam(
      @Named("gameId") Long gameId,
      @Named("playerIdList") List<Long> playerIdList,
      User user) {
    return gameProcessor.proposeTeam(gameId, getUserId(gameId, user), playerIdList);
  }

  /**
   * Issue a vote for the current team.
   */
  @ApiMethod(name="game.issueTeamVote", path="game/issue-team-vote")
  public Game issueTeamVote(
      @Named("gameId") Long gameId, @Named("approve") Boolean approve, User user) {
    return gameProcessor.issueTeamVote(gameId, getUserId(gameId, user), approve);
  }

  /**
   * Issue a vote for the current quest.
   */
  @ApiMethod(name="game.issueQuestVote", path="game/issue-quest-vote")
  public Game issueQuestVote(
      @Named("gameId") Long gameId, @Named("succeed") Boolean succeed, User user) {
    return gameProcessor.issueQuestVote(gameId, getUserId(gameId, user), succeed);
  }

  /**
   * Issue a guess at Merlin.
   */
  @ApiMethod(name="game.guessMerlin", path="game/guess-merlin")
  public Game guessMerlin(
      @Named("gameId") Long gameId, @Named("guessId") Long guessId, User user) {
    return gameProcessor.guessMerlin(gameId, getUserId(gameId, user), guessId);
  }
}
