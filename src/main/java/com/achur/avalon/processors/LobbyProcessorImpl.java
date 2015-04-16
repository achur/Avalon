package com.achur.avalon.processors;

import com.achur.avalon.entity.Game;
import com.achur.avalon.entity.Player;
import com.achur.avalon.storage.GameStore;
import com.achur.avalon.storage.PlayerStore;

import com.google.common.base.Function;
import com.google.inject.Inject;

import java.util.ArrayList;
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
    Game game = createNewGame();
    List<Player.Role> roles = new ArrayList<>();
    roles.add(Player.Role.MERLIN);
    if (includePercival) {
      roles.add(Player.Role.PERCIVAL);
    }
    if (includeMordred) {
      roles.add(Player.Role.MORDRED);
    }
    if (includeMorgana) {
      roles.add(Player.Role.MORGANA);
    }
    game.setRoles(roles);
    return gameStore.saveGame(game);
  }

  /**
   * Creates a starter game with defaults set.
   */
  private Game createNewGame() {
    Game game = new Game();
    game.setPlayers(new ArrayList<Long>());
    game.setState(Game.State.WAIT);
    game.setVoteCount(0);
    game.setQuestResults(new ArrayList<Boolean>());
    game.setCurrentTeam(new ArrayList<Long>());
    game.setCurrentLeader(null);
    game.setTeamVotesYay(new ArrayList<Long>());
    game.setTeamVotesNay(new ArrayList<Long>());
    game.setQuestVotesYay(new ArrayList<Long>());
    game.setQuestVotesNay(new ArrayList<Long>());
    game.setOutcome(null);

    return game;
  }

  /**
   * When adding players, we see if we have the minimum number of
   * necessary bad players to play (based on the number of players in
   * the game). If not, this returns true.
   */
  private boolean shouldAddBadPlayer(Game game) {
    int numBadNeeded = Constants.getNumBadPlayers(game.getPlayers().size());
    for (Player.Role role : game.getRoles()) {
      if (Constants.BAD_ROLES.contains(role)) {
        numBadNeeded--;
      }
    }
    return numBadNeeded > 0;
  }

  /**
   * {@inheritDoc}
   */
  public Player joinGame(Long id, String name, String email) {
    // Construct the player
    Player player = new Player();
    player.setGameId(id);
    player.setName(name);
    player.setEmail(email);
    final Player saved = playerStore.savePlayer(player);

    // Persist the player and modify the game state
    Function<Game, Game> modifier = new Function<Game, Game>() {
      @Override
      public Game apply(Game game) {
        game.getPlayers().add(saved.getId());
        boolean addBad = shouldAddBadPlayer(game);
        if (addBad) {
          if (game.getRoles().contains(Player.Role.ASSASSIN)) {
            game.getRoles().add(Player.Role.MINION);
          } else {
            game.getRoles().add(Player.Role.ASSASSIN);
          }
        } else {
          game.getRoles().add(Player.Role.SERVANT);
        }
        return game;
      }
    };
    gameStore.modifyGame(id, modifier);

    return player;
  }

  /**
   * {@inheritDoc}
   */
  public List<Game> listGames() {
    return gameStore.queryGames(Game.State.WAIT);
  }
}
