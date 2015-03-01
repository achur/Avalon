package com.achur.avalon.processors.inject;

import com.achur.avalon.processors.GameProcessor;
import com.achur.avalon.processors.GameProcessorImpl;
import com.achur.avalon.processors.LobbyProcessor;
import com.achur.avalon.processors.LobbyProcessorImpl;
import com.achur.avalon.storage.GameStore;
import com.achur.avalon.storage.PlayerStore;

import com.google.inject.AbstractModule;

/**
 * Avalon processor module. Binds storage implementations.
 */
public class AvalonProcessorModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(GameStore.class);
    requireBinding(PlayerStore.class);

    bind(GameProcessor.class).to(GameProcessorImpl.class);
    bind(LobbyProcessor.class).to(LobbyProcessorImpl.class);
  }
}
