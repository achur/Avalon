package com.achur.avalon.processors;

import com.achur.avalon.storage.GameStore;
import com.achur.avalon.storage.PlayerStore;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.Mock;
import org.mockito.Mockito;

@RunWith(MockitoJUnitRunner.class)
public class LobbyProcessorImplTest {

  @Mock GameStore gameStore;
  @Mock PlayerStore playerStore;

  private LobbyProcessorImpl lobbyProcessor;

  @Before
  public void setUp() {
    lobbyProcessor = new LobbyProcessorImpl(gameStore, playerStore);
  }

  @Test
  public void testCreateGame() {
    assertThat(1 + 3).isEqualTo(4);
  }
}
