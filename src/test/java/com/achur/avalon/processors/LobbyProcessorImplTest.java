package com.achur.avalon.processors;

import com.achur.avalon.entity.Game;
import com.achur.avalon.entity.Player;
import com.achur.avalon.storage.GameStore;
import com.achur.avalon.storage.PlayerStore;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.base.Function;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.HashSet;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class LobbyProcessorImplTest {

  @Mock GameStore gameStore;
  @Mock PlayerStore playerStore;

  private LobbyProcessorImpl lobbyProcessor;
  private long gameUid;
  private long playerUid;

  @Before
  @SuppressWarnings("unchecked")
  public void setUp() {
    lobbyProcessor = new LobbyProcessorImpl(gameStore, playerStore);

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

    // Mock out storage on save: it should create an ID if one is not
    // present and then reflect the value back.
    gameUid = 0;
    playerUid = 0;

    Mockito.doAnswer(new Answer<Game>() {
      public Game answer(InvocationOnMock invocation) {
        Object[] args = invocation.getArguments();
        Game game = (Game) args[0];
        if (game.getId() == null) {
          game.setId(gameUid++);
        }
        return game;
      }
    }).when(gameStore).saveGame(Mockito.any(Game.class));
    Mockito.doAnswer(new Answer<Player>() {
      public Player answer(InvocationOnMock invocation) {
        Object[] args = invocation.getArguments();
        Player player = (Player) args[0];
        if (player.getId() == null) {
          player.setId(playerUid++);
        }
        return player;
      }
    }).when(playerStore).savePlayer(Mockito.any(Player.class));
  }

  @Test
  public void createGameShouldAddSelectedRoles() {
    Game game = lobbyProcessor.createGame(true, false, true);
    assertThat(game.getRoles()).containsExactly(
        Player.Role.MERLIN, Player.Role.PERCIVAL, Player.Role.MORGANA);
  }

  @Test
  public void createdGameShouldBeInWaitState() {
    Game game = lobbyProcessor.createGame(false, false, false);
    assertThat(game.getState()).isEqualTo(Game.State.WAIT);
  }

  @Test
  public void createGameShouldCreateUniqueSyntheticId() {
    Game game0 = lobbyProcessor.createGame(true, false, true);
    Game game1 = lobbyProcessor.createGame(true, false, true);
    Game game2 = lobbyProcessor.createGame(true, false, true);

    assertThat(game0.getId()).isNotNull();
    assertThat(game1.getId()).isNotNull();
    assertThat(game2.getId()).isNotNull();

    Set<Long> ids = new HashSet<>();
    ids.add(game0.getId());
    ids.add(game1.getId());
    ids.add(game2.getId());

    assertThat(ids.size()).isEqualTo(3);
  }

  @Test
  public void joinGameShouldAssignAssassinWhenNotEnoughBadRolesAndNoAssassin() {
    Game game = lobbyProcessor.createGame(true, true, false);
    assertThat(game.getRoles()).containsNoneOf(
        Player.Role.ASSASSIN, Player.Role.MORGANA, Player.Role.SERVANT, Player.Role.MINION);
    Mockito.when(gameStore.getGame(Mockito.any(Long.class))).thenReturn(game);

    lobbyProcessor.joinGame(0L, "Alex", "al.churchill@gmail.com");

    assertThat(game.getRoles()).contains(Player.Role.ASSASSIN);
  }

  @Test
  public void joinGameShouldAssignMinionWhenNotEnoughBadRolesAndAssassinPresent() {
    Game game = lobbyProcessor.createGame(true, false, false);
    game.getRoles().add(Player.Role.ASSASSIN);
    assertThat(game.getRoles()).containsNoneOf(
        Player.Role.MORGANA, Player.Role.MORDRED, Player.Role.SERVANT, Player.Role.MINION);
    Mockito.when(gameStore.getGame(Mockito.any(Long.class))).thenReturn(game);

    lobbyProcessor.joinGame(0L, "Alex", "al.churchill@gmail.com");

    assertThat(game.getRoles()).contains(Player.Role.MINION);
  }

  @Test
  public void joinGameShouldAssignServantWhenEnoughBadRolesPresent() {
    Game game = lobbyProcessor.createGame(true, true, true);
    assertThat(game.getRoles()).containsNoneOf(
        Player.Role.ASSASSIN, Player.Role.SERVANT, Player.Role.MINION);
    Mockito.when(gameStore.getGame(Mockito.any(Long.class))).thenReturn(game);

    lobbyProcessor.joinGame(0L, "Alex", "al.churchill@gmail.com");

    assertThat(game.getRoles()).contains(Player.Role.SERVANT);
  }

  @Test
  public void listGamesShouldReturnOutputFromGameStore() {
    lobbyProcessor.listGames();
    Mockito.verify(gameStore).queryGames(Game.State.WAIT);
  }
}
