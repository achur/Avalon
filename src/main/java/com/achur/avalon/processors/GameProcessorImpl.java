package com.achur.avalon.processors;

import com.achur.avalon.entity.Game;
import com.achur.avalon.entity.Player;
import com.achur.avalon.storage.GameStore;
import com.achur.avalon.storage.PlayerStore;

import com.google.inject.Inject;

import java.util.List;

public class GameProcessorImpl implements GameProcessor {

  /**
   * The Game Store service which handles persistance of Game objects.
   */
  private GameStore gameStore;

  /**
   * The Player Store service which handles persistance of Player objects.
   */
  private PlayerStore playerStore;

  /**
   * Guice-injected constructor.
   */
  @Inject
  GameProcessorImpl(GameStore gameStore, PlayerStore playerStore) {
    this.gameStore = gameStore;
    this.playerStore = playerStore;
  }


  /**
   * {@inheritDoc}
   */
  public Game startGame(Long id) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public Game startVoting(Long id) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public Game proposeTeam(Long gameId, Long proposerId, List<Long> playerIdList) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public Game issueTeamVote(Long gameId, Long voterId, Boolean approve) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public Game issueQuestVote(Long gameId, Long voterId, Boolean succeed) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public Game guessMerlin(Long gameId, Long voterId, Long guessId) {
    throw new UnsupportedOperationException();
  }
}
