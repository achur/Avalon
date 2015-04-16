package com.achur.avalon.processors;

import com.achur.avalon.entity.Game;
import com.achur.avalon.entity.Player;
import com.achur.avalon.storage.GameStore;
import com.achur.avalon.storage.PlayerStore;

import com.google.common.base.Function;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameProcessorImpl implements GameProcessor {

  /**
   * The Game Store service which handles persistance of Game objects.
   */
  private final GameStore gameStore;

  /**
   * The Player Store service which handles persistance of Player objects.
   */
  private final PlayerStore playerStore;

  /**
   * Guice-injected constructor.
   */
  @Inject
  GameProcessorImpl(final GameStore gameStore, final PlayerStore playerStore) {
    this.gameStore = gameStore;
    this.playerStore = playerStore;
  }


  /**
   * {@inheritDoc}
   */
  public Game startGame(Long id) {
    Function<Game, Game> modifier = new Function<Game, Game>() {
      @Override
      public Game apply(Game game) {
        assignPlayers(game);
        game.setState(Game.State.START);
        return game;
      }
    };
    return gameStore.modifyGame(id, modifier);
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
    Function<Game, Game> modifier = new Function<Game, Game>() {
      @Override
      public Game apply(Game game) {
        game.setState(Game.State.TEAM_SELECTION);
        return game;
      }
    };
    return gameStore.modifyGame(id, modifier);
  }

  /**
   * {@inheritDoc}
   */
  public Game proposeTeam(Long gameId, final Long proposerId, final List<Long> playerIdList) {
    Function<Game, Game> modifier = new Function<Game, Game>() {
      @Override
      public Game apply(Game game) {
        // TODO(achur): Here and elsewhere, use something better than assert.
        assert game.getState() == Game.State.TEAM_SELECTION : "Wrong state";
        assert game.getCurrentLeader() == proposerId : "Wrong proposer";

        // TODO(achur): Logic to determine if the right number of players is questing.

        game.setCurrentTeam(playerIdList);
        clearTeamVotes(game);
        game.setState(Game.State.TEAM_VOTING);
        return game;
      }
    };
    return gameStore.modifyGame(gameId, modifier);
  }

  private void clearTeamVotes(Game game) {
    game.setTeamVotesYay(new ArrayList<Long>());
    game.setTeamVotesNay(new ArrayList<Long>());
  }

  private void clearQuestVotes(Game game) {
    game.setQuestVotesYay(new ArrayList<Long>());
    game.setQuestVotesNay(new ArrayList<Long>());
  }

  private void issueVote(
      List<Long> votesYay, List<Long> votesNay, Long voterId, Boolean approve) {
    if (votesYay.contains(voterId)) {
      if (!approve) {
        votesYay.remove(voterId);
        votesNay.add(voterId);
      }
    } else if (votesNay.contains(voterId)) {
      if (approve) {
        votesNay.remove(voterId);
        votesYay.add(voterId);
      }
    } else {
      if (approve) {
        votesYay.add(voterId);
      } else {
        votesNay.add(voterId);
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
  public Game issueTeamVote(Long gameId, final Long voterId, final Boolean approve) {
    Function<Game, Game> modifier = new Function<Game, Game>() {
      @Override
      public Game apply(Game game) {
        assert game.getState() == Game.State.TEAM_VOTING : "Wrong state";

        issueVote(game.getTeamVotesYay(), game.getTeamVotesNay(), voterId, approve);

        // If all votes are in, check if we're going questing.
        if (game.getTeamVotesYay().size() + game.getTeamVotesNay().size() ==
            game.getPlayers().size()) {
          if (game.getTeamVotesYay().size() > game.getTeamVotesNay().size()) {
            game.setState(Game.State.QUEST);
            clearQuestVotes(game);
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
        return game;
      }
    };
    return gameStore.modifyGame(gameId, modifier);
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
  public Game issueQuestVote(Long gameId, final Long voterId, final Boolean succeed) {
    Function<Game, Game> modifier = new Function<Game, Game>() {
      @Override
      public Game apply(Game game) {
        assert game.getState() == Game.State.QUEST : "Wrong state";

        assert game.getCurrentTeam().contains(voterId) : "You must be on the team to vote";

        if (!succeed) {
          Player voter = playerStore.getPlayer(voterId);
          assert Constants.BAD_ROLES.contains(voter.getRole()) :
              "Only bad players can vote to fail a quest";
        }
        issueVote(game.getQuestVotesYay(), game.getQuestVotesNay(), voterId, succeed);

        // If all votes are in, check the result of the quest.
        if (game.getQuestVotesYay().size() + game.getQuestVotesNay().size() ==
            Constants.getNumPlayers(game.getPlayers().size(), game.getQuestResults().size())) {
          boolean result = game.getQuestVotesNay().size() < Constants.getNumFailsRequired(
              game.getPlayers().size(), game.getQuestResults().size());
          game.getQuestResults().add(result);
          if (countResults(game, true) > Constants.NUM_QUESTS / 2) {
            game.setState(Game.State.GUESS_MERLIN);
          } else if (countResults(game, false) > Constants.NUM_QUESTS / 2) {
            game.setOutcome(false);
            game.setState(Game.State.END);
          } else {
            game.setCurrentLeader(getNextLeader(game));
            game.setState(Game.State.TEAM_SELECTION);
          }
        }
        return game;
      }
    };
    return gameStore.modifyGame(gameId, modifier);
  }

  /**
   * {@inheritDoc}
   */
  public Game guessMerlin(Long gameId, final Long voterId, final Long guessId) {
    Function<Game, Game> modifier = new Function<Game, Game>() {
      @Override
      public Game apply(Game game) {
        assert game.getState() == Game.State.GUESS_MERLIN : "Wrong state";

        Player voter = playerStore.getPlayer(voterId);

        assert Constants.BAD_ROLES.contains(voter.getRole()) :
            "Only bad players can guess Merlin";

        Player guess = playerStore.getPlayer(guessId);
        game.setOutcome(guess.getRole() != Player.Role.MERLIN);
        game.setState(Game.State.END);
        return game;
      }
    };
    return gameStore.modifyGame(gameId, modifier);
  }
}
