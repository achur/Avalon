package com.achur.avalon.processors;

import com.achur.avalon.entity.Game;
import com.achur.avalon.entity.Player;
import com.achur.avalon.storage.GameStore;
import com.achur.avalon.storage.PlayerStore;

import java.util.List;

public class LobbyProcessorImpl implements LobbyProcessor {

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
