package com.achur.avalon.processors;

import com.achur.avalon.entity.Game;
import com.achur.avalon.entity.Player;

import java.util.List;

public interface LobbyProcessor {

  /**
   * Creates a new game in the WAIT state.
   *
   * @return The game that was created.
   */
  public Game createGame(Boolean includePercival, Boolean includeMordred, Boolean includeMorgana);

  /**
   * Joins a game in the WAIT state.
   *
   * @param id The ID of the game to join.
   * @return The player that just joined the game.
   */
  public Player joinGame(Long id);

  /**
   * Lists available games in the WAIT state.
   *
   * @return A list of game objects for the available games.
   */
  public List<Game> listGames();
}
