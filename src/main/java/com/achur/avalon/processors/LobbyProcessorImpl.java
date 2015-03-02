package com.achur.avalon.processors;

import com.achur.avalon.entity.Game;
import com.achur.avalon.entity.Player;
import com.achur.avalon.storage.GameStore;
import com.achur.avalon.storage.PlayerStore;

import com.google.inject.Inject;

import java.util.List;

public class LobbyProcessorImpl implements LobbyProcessor {

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
  LobbyProcessorImpl(GameStore gameStore, PlayerStore playerStore) {
    this.gameStore = gameStore;
    this.playerStore = playerStore;
  }


  /**
   * {@inheritDoc}
   */
  public Game createGame(Boolean includePercival, Boolean includeMordred, Boolean includeMorgana) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public Player joinGame(Long id) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public List<Game> listGames() {
    throw new UnsupportedOperationException();
  }
}
