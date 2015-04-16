package com.achur.avalon.processors;

import com.achur.avalon.entity.Game;
import com.achur.avalon.entity.Player;
import com.achur.avalon.storage.GameStore;
import com.achur.avalon.storage.PlayerStore;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GameProcessorImplTest {

  private static final Long GAME_ID = 123L;

  @Mock GameStore gameStore;
  @Mock PlayerStore playerStore;

  private GameProcessorImpl gameProcessor;

  @Before
  @SuppressWarnings("unchecked")
  public void setUp() {
    gameProcessor = new GameProcessorImpl(gameStore, playerStore);

    // Mock the behavior of atomic actions to test the logic of those
    // actions. This isn't great, but gets the job done.
    Mockito.doAnswer(new Answer<Game>() {
      public Game answer(InvocationOnMock invocation) {
        Object[] args = invocation.getArguments();
        Long id = (Long) args[0];
        Function<Game, Game> modifier = (Function<Game, Game>) args[1];
        return modifier.apply(gameStore.getGame(id));
      }
    }).when(gameStore).modifyGame(Mockito.any(Long.class), Mockito.any(Function.class));
  }

  private Player constructPlayer(Long id) {
    Player player = new Player();
    player.setId(id);
    return player;
  }

  private Game constructGame(Game.State state) {
    Game game = new Game();
    game.setId(GAME_ID);
    game.setCurrentLeader(1L);
    game.setVoteCount(0);
    game.setCurrentTeam(new ArrayList<Long>());
    game.setTeamVotesYay(new ArrayList<Long>());
    game.setTeamVotesNay(new ArrayList<Long>());
    game.setQuestVotesYay(new ArrayList<Long>());
    game.setQuestVotesNay(new ArrayList<Long>());
    game.setQuestResults(new ArrayList<Boolean>());
    if (state != null) {
      game.setState(state);
    }
    game.setRoles(ImmutableList.<Player.Role>of(
        Player.Role.MERLIN,
        Player.Role.PERCIVAL,
        Player.Role.MORDRED,
        Player.Role.ASSASSIN,
        Player.Role.SERVANT));
    game.setPlayers(ImmutableList.of(0L, 1L, 2L, 3L, 4L));
    return game;
  }

  private Game mockGame(Game.State state) {
    Game game = constructGame(state);
    Mockito.when(gameStore.getGame(Mockito.any(Long.class))).thenReturn(game);
    return game;
  }

  private List<Player> mockPlayerList() {
    List<Player> players = ImmutableList.of(
        constructPlayer(0L),
        constructPlayer(1L),
        constructPlayer(2L),
        constructPlayer(3L),
        constructPlayer(4L));
    for (Player player : players) {
      Mockito.when(playerStore.getPlayer(player.getId())).thenReturn(player);
    }
    return players;
  }

  @Test
  public void startGameShouldChangeStateToStart() {
    Game game = mockGame(Game.State.WAIT);
    List<Player> players = mockPlayerList();

    gameProcessor.startGame(GAME_ID);
    assertThat(game.getState()).isEqualTo(Game.State.START);
  }

  @Test
  public void startGameAssignsRoles() {
    Game game = mockGame(Game.State.WAIT);
    List<Player> players = mockPlayerList();

    gameProcessor.startGame(GAME_ID);

    List<Player.Role> assignedRoles = new ArrayList<>();
    for (Player player : players) {
      assignedRoles.add(player.getRole());
    }
    assertThat(assignedRoles).containsExactly(
        Player.Role.MERLIN,
        Player.Role.PERCIVAL,
        Player.Role.MORDRED,
        Player.Role.ASSASSIN,
        Player.Role.SERVANT);
  }

  @Test
  public void startSelectionShouldChangeStateToTeamSelection() {
    Game game = mockGame(Game.State.START);
    List<Player> players = mockPlayerList();

    gameProcessor.startSelection(GAME_ID);
    assertThat(game.getState()).isEqualTo(Game.State.TEAM_SELECTION);
  }

  @Test(expected=AssertionError.class)
  public void proposeTeamMustBeInTeamSelectionState() {
    Game game = mockGame(Game.State.START);
    List<Player> players = mockPlayerList();

    gameProcessor.proposeTeam(GAME_ID, 1L, ImmutableList.<Long>of(1L, 2L));
  }

  @Test(expected=AssertionError.class)
  public void proposeTeamMustBeByCorrectProposer() {
    Game game = mockGame(Game.State.TEAM_SELECTION);
    game.setCurrentLeader(2L);
    List<Player> players = mockPlayerList();

    gameProcessor.proposeTeam(GAME_ID, 1L, ImmutableList.<Long>of(1L, 2L));
  }

  @Test
  public void proposeTeamShouldChangeStateToTeamVoting() {
    Game game = mockGame(Game.State.TEAM_SELECTION);
    List<Player> players = mockPlayerList();

    gameProcessor.proposeTeam(GAME_ID, 1L, ImmutableList.<Long>of(1L, 2L));

    assertThat(game.getState()).isEqualTo(Game.State.TEAM_VOTING);
  }

  @Test
  public void proposeTeamShouldSetProposedTeam() {
    Game game = mockGame(Game.State.TEAM_SELECTION);
    List<Player> players = mockPlayerList();

    gameProcessor.proposeTeam(GAME_ID, 1L, ImmutableList.<Long>of(1L, 2L));

    assertThat(game.getCurrentTeam()).containsExactly(1L, 2L);
  }

  @Test(expected=AssertionError.class)
  public void issueTeamVoteMustBeInTeamVotingState() {
    Game game = mockGame(Game.State.START);
    List<Player> players = mockPlayerList();

    gameProcessor.issueTeamVote(GAME_ID, 1L, true);
  }

  @Test
  public void issueTeamVoteShouldRecordVote() {
    Game game = mockGame(Game.State.TEAM_VOTING);
    List<Player> players = mockPlayerList();

    gameProcessor.issueTeamVote(GAME_ID, 1L, true);
    gameProcessor.issueTeamVote(GAME_ID, 2L, false);
    gameProcessor.issueTeamVote(GAME_ID, 3L, false);
    gameProcessor.issueTeamVote(GAME_ID, 2L, true);

    assertThat(game.getTeamVotesYay()).containsExactly(1L, 2L);
    assertThat(game.getTeamVotesNay()).containsExactly(3L);
  }

  @Test
  public void issueTeamVoteShouldHandlePassingVote() {
    Game game = mockGame(Game.State.TEAM_VOTING);
    List<Player> players = mockPlayerList();

    gameProcessor.issueTeamVote(GAME_ID, 0L, true);
    gameProcessor.issueTeamVote(GAME_ID, 1L, false);
    gameProcessor.issueTeamVote(GAME_ID, 2L, true);
    gameProcessor.issueTeamVote(GAME_ID, 3L, false);
    gameProcessor.issueTeamVote(GAME_ID, 4L, true);

    assertThat(game.getState()).isEqualTo(Game.State.QUEST);
  }

  @Test
  public void issueTeamVoteShouldHandleFailingVote() {
    Game game = mockGame(Game.State.TEAM_VOTING);
    List<Player> players = mockPlayerList();

    gameProcessor.issueTeamVote(GAME_ID, 0L, false);
    gameProcessor.issueTeamVote(GAME_ID, 1L, false);
    gameProcessor.issueTeamVote(GAME_ID, 2L, true);
    gameProcessor.issueTeamVote(GAME_ID, 3L, false);
    gameProcessor.issueTeamVote(GAME_ID, 4L, true);

    assertThat(game.getState()).isEqualTo(Game.State.TEAM_SELECTION);
    assertThat(game.getCurrentLeader()).isEqualTo(2L);
    assertThat(game.getVoteCount()).isEqualTo(1);
  }

  @Test
  public void issueTeamVoteShouldHandleGameLosingVote() {
    Game game = mockGame(Game.State.TEAM_VOTING);
    game.setVoteCount(Constants.MAX_VOTES - 1);
    List<Player> players = mockPlayerList();

    gameProcessor.issueTeamVote(GAME_ID, 0L, false);
    gameProcessor.issueTeamVote(GAME_ID, 1L, false);
    gameProcessor.issueTeamVote(GAME_ID, 2L, true);
    gameProcessor.issueTeamVote(GAME_ID, 3L, false);
    gameProcessor.issueTeamVote(GAME_ID, 4L, true);

    assertThat(game.getState()).isEqualTo(Game.State.END);
    assertThat(game.getOutcome()).isEqualTo(false);
  }

  @Test(expected=AssertionError.class)
  public void issueQuestVoteMustBeInQuestState() {
    Game game = mockGame(Game.State.TEAM_VOTING);
    List<Player> players = mockPlayerList();

    gameProcessor.issueQuestVote(GAME_ID, 1L, true);
  }

  @Test(expected=AssertionError.class)
  public void issueQuestVoteShouldNotAllowGoodPlayersToFail() {
    Game game = mockGame(Game.State.QUEST);
    List<Player> players = mockPlayerList();
    players.get(1).setRole(Player.Role.MERLIN);

    gameProcessor.issueQuestVote(GAME_ID, 1L, false);
  }

  @Test(expected=AssertionError.class)
  public void issueQuestVoteShouldOnlyAllowCurrentTeamPlayers() {
    Game game = mockGame(Game.State.QUEST);
    game.setCurrentTeam(ImmutableList.of(2L, 3L));
    List<Player> players = mockPlayerList();

    gameProcessor.issueQuestVote(GAME_ID, 1L, true);
  }

  @Test
  public void issueQuestVoteShouldRecordVote() {
    Game game = mockGame(Game.State.QUEST);
    game.setCurrentTeam(ImmutableList.of(1L, 2L));
    List<Player> players = mockPlayerList();
    players.get(2).setRole(Player.Role.MORDRED);

    gameProcessor.issueQuestVote(GAME_ID, 1L, true);
    gameProcessor.issueQuestVote(GAME_ID, 2L, false);

    assertThat(game.getQuestVotesYay()).containsExactly(1L);
    assertThat(game.getQuestVotesNay()).containsExactly(2L);
  }

  @Test
  public void issueQuestVoteShouldHandleWinningQuest() {
    Game game = mockGame(Game.State.QUEST);
    game.setCurrentTeam(ImmutableList.of(1L, 2L));
    List<Player> players = mockPlayerList();

    gameProcessor.issueQuestVote(GAME_ID, 1L, true);
    gameProcessor.issueQuestVote(GAME_ID, 2L, true);

    assertThat(game.getState()).isEqualTo(Game.State.TEAM_SELECTION);
    assertThat(game.getCurrentLeader()).isEqualTo(2L);
    assertThat(game.getQuestResults()).containsExactly(true);
  }

  @Test
  public void issueQuestVoteShouldHandleLosingQuest() {
    Game game = mockGame(Game.State.QUEST);
    List<Player> players = mockPlayerList();
    game.setCurrentTeam(ImmutableList.of(1L, 2L));
    players.get(2).setRole(Player.Role.MORDRED);

    gameProcessor.issueQuestVote(GAME_ID, 1L, true);
    gameProcessor.issueQuestVote(GAME_ID, 2L, false);

    assertThat(game.getState()).isEqualTo(Game.State.TEAM_SELECTION);
    assertThat(game.getCurrentLeader()).isEqualTo(2L);
    assertThat(game.getQuestResults()).containsExactly(false);
  }

  @Test
  public void issueQuestVoteShouldHandleGameWinningVote() {
    Game game = mockGame(Game.State.QUEST);
    game.setCurrentTeam(ImmutableList.of(1L, 2L, 3L));
    List<Boolean> results = new ArrayList<>();
    results.add(true);
    results.add(false);
    results.add(true);
    game.setQuestResults(results);
    List<Player> players = mockPlayerList();

    gameProcessor.issueQuestVote(GAME_ID, 1L, true);
    gameProcessor.issueQuestVote(GAME_ID, 2L, true);
    gameProcessor.issueQuestVote(GAME_ID, 3L, true);

    assertThat(game.getState()).isEqualTo(Game.State.GUESS_MERLIN);
  }

  @Test
  public void issueQuestVoteShouldHandleGameLosingVote() {
    Game game = mockGame(Game.State.QUEST);
    game.setCurrentTeam(ImmutableList.of(1L, 2L, 3L));
    List<Boolean> results = new ArrayList<>();
    results.add(false);
    results.add(true);
    results.add(false);
    game.setQuestResults(results);
    List<Player> players = mockPlayerList();
    players.get(2).setRole(Player.Role.MORDRED);

    gameProcessor.issueQuestVote(GAME_ID, 1L, true);
    gameProcessor.issueQuestVote(GAME_ID, 2L, false);
    gameProcessor.issueQuestVote(GAME_ID, 3L, true);

    assertThat(game.getState()).isEqualTo(Game.State.END);
    assertThat(game.getOutcome()).isEqualTo(false);
  }

  @Test(expected=AssertionError.class)
  public void guessMerlinShouldBeInGuessMerlinState() {
    Game game = mockGame(Game.State.END);
    List<Player> players = mockPlayerList();

    gameProcessor.guessMerlin(GAME_ID, 1L, 2L);
  }

  @Test(expected=AssertionError.class)
  public void guessMerlinShouldRestrictToBadPlayers() {
    Game game = mockGame(Game.State.GUESS_MERLIN);
    List<Player> players = mockPlayerList();
    players.get(1).setRole(Player.Role.PERCIVAL);

    gameProcessor.guessMerlin(GAME_ID, 1L, 2L);
  }

  @Test
  public void guessMerlinShouldHandleWrongGuess() {
    Game game = mockGame(Game.State.GUESS_MERLIN);
    List<Player> players = mockPlayerList();
    players.get(1).setRole(Player.Role.MORDRED);
    players.get(2).setRole(Player.Role.PERCIVAL);

    gameProcessor.guessMerlin(GAME_ID, 1L, 2L);

    assertThat(game.getOutcome()).isEqualTo(true);
    assertThat(game.getState()).isEqualTo(Game.State.END);
  }

  @Test
  public void guessMerlinShouldHandleRightGuess() {
    Game game = mockGame(Game.State.GUESS_MERLIN);
    List<Player> players = mockPlayerList();
    players.get(1).setRole(Player.Role.MORDRED);
    players.get(2).setRole(Player.Role.MERLIN);

    gameProcessor.guessMerlin(GAME_ID, 1L, 2L);

    assertThat(game.getOutcome()).isEqualTo(false);
    assertThat(game.getState()).isEqualTo(Game.State.END);
  }
}
