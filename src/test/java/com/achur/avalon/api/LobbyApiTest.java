package com.achur.avalon.api;

import com.achur.avalon.entity.Game;
import com.achur.avalon.entity.Player;
import com.achur.avalon.processors.LobbyProcessor;

import static com.google.common.truth.Truth.assertThat;

import com.google.appengine.api.users.User;
import com.google.common.collect.ImmutableList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class LobbyApiTest {

  @Mock LobbyProcessor lobbyProcessor;

  private LobbyApi lobbyApi;

  private static Game constructGame(Long id) {
    Game game = new Game();
    game.setId(id);
    return game;
  }

  private static Player constructPlayer(Long id) {
    Player player = new Player();
    player.setId(id);
    return player;
  }

  private static final String NAME = "al.churchill";
  private static final String EMAIL = "al.churchill@gmail.com";

  private static final List<Game> GAMES = ImmutableList.of(
      constructGame(0L),
      constructGame(1L));

  private static final Player PLAYER = constructPlayer(8L);

  @Before
  public void setUp() {
    lobbyApi = new LobbyApi(lobbyProcessor);
  }

  @Test
  public void listGamesShouldCallListGames() {
    Mockito.when(lobbyProcessor.listGames()).thenReturn(GAMES);
    assertThat(lobbyApi.listGames()).isEqualTo(GAMES);
  }

  @Test
  public void joinGameShouldCallJoinGame() {
    Mockito.when(lobbyProcessor.joinGame(0L, NAME, EMAIL)).thenReturn(PLAYER);
    User user = new User(EMAIL, "gmail.com");
    assertThat(lobbyApi.joinGame(0L, user)).isEqualTo(PLAYER);
  }

  @Test
  public void createGameShouldCallCreateGame() {
    Mockito.when(lobbyProcessor.createGame(false, true, false)).thenReturn(GAMES.get(0));
    assertThat(lobbyApi.createGame(false, true, null)).isEqualTo(GAMES.get(0));
  }
}
