package com.achur.avalon.api;

import com.achur.avalon.entity.Game;
import com.achur.avalon.entity.Player;
import com.achur.avalon.processors.GameProcessor;

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
public class GameApiTest {

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

  private static final Long GAME_ID = 0L;
  private static final Game GAME = constructGame(GAME_ID);

  private static final String NAME = "al.churchill";
  private static final String EMAIL = "al.churchill@gmail.com";
  private static final User USER = new User(EMAIL, "gmail.com");

  private static final Long PLAYER_ID = 1L;
  private static final Player PLAYER = constructPlayer(PLAYER_ID);


  private static final List<Long> ID_LIST = ImmutableList.of(10L, 20L);
  private static final boolean TEAM_VOTE = false;
  private static final boolean QUEST_VOTE = true;
  private static final Long GUESS_ID = 20L;

  @Mock GameProcessor gameProcessor;

  private GameApi gameApi;

  @Before
  public void setUp() {
    gameApi = new GameApi(gameProcessor);
  }

  @Test
  public void getGameShouldCallGetGame() {
    Mockito.when(gameProcessor.getGame(GAME_ID)).thenReturn(GAME);
    assertThat(gameApi.getGame(GAME_ID)).isEqualTo(GAME);
  }

  @Test
  public void startGameShouldCallStartGame() {
    Mockito.when(gameProcessor.startGame(GAME_ID)).thenReturn(GAME);
    assertThat(gameApi.startGame(GAME_ID)).isEqualTo(GAME);
  }

  @Test
  public void selectShouldCallStartTeam() {
    Mockito.when(gameProcessor.startSelection(GAME_ID)).thenReturn(GAME);
    assertThat(gameApi.startSelection(GAME_ID)).isEqualTo(GAME);
  }

  @Test
  public void proposeTeamShouldCallProposeTeam() {
    Mockito.when(gameProcessor.getPlayer(GAME_ID, EMAIL)).thenReturn(PLAYER);
    Mockito.when(gameProcessor.proposeTeam(GAME_ID, PLAYER_ID, ID_LIST)).thenReturn(GAME);
    assertThat(gameApi.proposeTeam(GAME_ID, ID_LIST, USER)).isEqualTo(GAME);
  }

  @Test
  public void issueTeamVoteShouldCallIssueTeamVote() {
    Mockito.when(gameProcessor.getPlayer(GAME_ID, EMAIL)).thenReturn(PLAYER);
    Mockito.when(gameProcessor.issueTeamVote(GAME_ID, PLAYER_ID, TEAM_VOTE)).thenReturn(GAME);
    assertThat(gameApi.issueTeamVote(GAME_ID, TEAM_VOTE, USER)).isEqualTo(GAME);
  }

  @Test
  public void issueQuestVoteShouldCallIssueQuestVote() {
    Mockito.when(gameProcessor.getPlayer(GAME_ID, EMAIL)).thenReturn(PLAYER);
    Mockito.when(gameProcessor.issueQuestVote(GAME_ID, PLAYER_ID, QUEST_VOTE)).thenReturn(GAME);
    assertThat(gameApi.issueQuestVote(GAME_ID, QUEST_VOTE, USER)).isEqualTo(GAME);
  }

  @Test
  public void guessMerlinShouldCallGuessMerlin() {
    Mockito.when(gameProcessor.getPlayer(GAME_ID, EMAIL)).thenReturn(PLAYER);
    Mockito.when(gameProcessor.guessMerlin(GAME_ID, PLAYER_ID, GUESS_ID)).thenReturn(GAME);
    assertThat(gameApi.guessMerlin(GAME_ID, GUESS_ID, USER)).isEqualTo(GAME);
  }
}
