package com.achur.avalon.processors;

import com.achur.avalon.entity.Game;
import com.achur.avalon.entity.Player;
import com.achur.avalon.storage.GameStore;
import com.achur.avalon.storage.PlayerStore;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Collections;
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
    Game game = gameStore.getGame(id);
    assignPlayers(game);
    game.setState(Game.State.START);
    gameStore.saveGame(game);
    return game;
  }

  /**
   * Shuffle the roles in the game and randomly assign the players.
   */
  private void assignPlayers(Game game) {
    ArrayList<Player.Role> roles = new ArrayList<>(game.getRoles());
    Collections.shuffle(roles);
    for (int i = 0; i < game.getPlayers().size(); ++i) {
      Long playerId = game.getPlayers().get(i);
      Player player = playerStore.getPlayer(playerId);
      player.setRole(roles.get(i));
      playerStore.savePlayer(player);
    }
  }

  /**
   * {@inheritDoc}
   */
  public Game startSelection(Long id) {
    Game game = gameStore.getGame(id);
    game.setState(Game.State.TEAM_SELECTION);
    gameStore.saveGame(game);
    return game;
  }

  /**
   * {@inheritDoc}
   */
  public Game proposeTeam(Long gameId, Long proposerId, List<Long> playerIdList) {
    Game game = gameStore.getGame(gameId);

    // TODO(achur): Here and elsewhere, use something better than assert.
    assert game.getState() == Game.State.TEAM_SELECTION : "Wrong state";
    assert game.getCurrentLeader() == proposerId : "Wrong proposer";

    // TODO(achur): Logic to determine if the right number of players is questing.

    game.setCurrentTeam(playerIdList);
    game.setState(Game.State.TEAM_VOTING);
    gameStore.saveGame(game);
    return game;
  }

  private void issueVote(Game game, Long voterId, Boolean approve) {
    if (game.getVotesYay().contains(voterId)) {
      if (!approve) {
        game.getVotesYay().remove(voterId);
        game.getVotesNay().add(voterId);
      }
    } else if (game.getVotesNay().contains(voterId)) {
      if (approve) {
        game.getVotesNay().remove(voterId);
        game.getVotesYay().add(voterId);
      }
    } else {
      if (approve) {
        game.getVotesYay().add(voterId);
      } else {
        game.getVotesNay().add(voterId);
      }
    }
  }

  /**
   * @param game The game in question.
   * @return The ID of the next team leader.
   */
  private Long getNextLeader(Game game) {
    return getNextPlayer(game, game.getCurrentLeader());
  }

  /**
   * Fetches the next player in a game's list of players, rotating around when
   * hitting the end of the list.
   *
   * @param game The game in question
   * @param playerId The ID of the current player in the list.
   * @return The next player in the list.
   */
  private Long getNextPlayer(Game game, Long playerId) {
    int index = game.getPlayers().indexOf(playerId);
    assert index >= 0 : "Player " + playerId + " not found";
    int next = (index + 1) % game.getPlayers().size();
    return game.getPlayers().get(next);
  }

  /**
   * {@inheritDoc}
   */
  public Game issueTeamVote(Long gameId, Long voterId, Boolean approve) {
    Game game = gameStore.getGame(gameId);

    assert game.getState() == Game.State.TEAM_VOTING : "Wrong state";

    issueVote(game, voterId, approve);

    // If all votes are in, check if we're going questing.
    if (game.getVotesYay().size() + game.getVotesNay().size() ==
        game.getPlayers().size()) {
      if (game.getVotesYay().size() > game.getVotesNay().size()) {
        game.setState(Game.State.QUEST);
      } else {
        game.setVoteCount(game.getVoteCount() + 1);
        if (game.getVoteCount() >= Constants.MAX_VOTES) {
          game.setOutcome(false);
          game.setState(Game.State.END);
        } else {
          game.setCurrentLeader(getNextLeader(game));
          game.setState(Game.State.TEAM_SELECTION);
        }
      }
    }
    gameStore.saveGame(game);
    return game;
  }

  /**
   * @param game The game in question
   * @param success Whether we are counting successes
   * @return The number of quests with the given result so far in the game.
   */
  private int countResults(Game game, Boolean success) {
    int count = 0;
    for (Boolean result : game.getQuestResults()) {
      if (result == success) {
        count++;
      }
    }
    return count;
  }

  /**
   * {@inheritDoc}
   */
  public Game issueQuestVote(Long gameId, Long voterId, Boolean succeed) {
    Game game = gameStore.getGame(gameId);

    assert game.getState() == Game.State.QUEST : "Wrong state";

    if (!succeed) {
      Player voter = playerStore.getPlayer(voterId);
      assert Constants.BAD_ROLES.contains(voter.getRole()) :
          "Only bad players can vote to fail a quest";
    }
    issueVote(game, voterId, succeed);

    // If all votes are in, check the result of the quest.
    if (game.getVotesYay().size() + game.getVotesNay().size() ==
        Constants.getNumPlayers(game.getPlayers().size(), game.getQuestResults().size())) {
      boolean result = game.getVotesNay().size() < Constants.getNumFailsRequired(
          game.getPlayers().size(), game.getQuestResults().size());
      game.getQuestResults().add(result);
      if (countResults(game, true) > Constants.NUM_QUESTS / 2) {
        game.setState(Game.State.GUESS_MERLIN);
      } else {
        game.setOutcome(false);
        game.setState(Game.State.END);
      }
    } else {
      game.setCurrentLeader(getNextLeader(game));
      game.setState(Game.State.TEAM_SELECTION);
    }

    gameStore.saveGame(game);
    return game;
  }

  /**
   * {@inheritDoc}
   */
  public Game guessMerlin(Long gameId, Long voterId, Long guessId) {
    Game game = gameStore.getGame(gameId);

    assert game.getState() == Game.State.GUESS_MERLIN : "Wrong state";

    Player voter = playerStore.getPlayer(voterId);

    assert Constants.BAD_ROLES.contains(voter.getRole()) :
        "Only bad players can guess Merlin";

    Player guess = playerStore.getPlayer(guessId);
    game.setOutcome(guess.getRole() != Player.Role.MERLIN);
    gameStore.saveGame(game);
    return game;
  }
}
