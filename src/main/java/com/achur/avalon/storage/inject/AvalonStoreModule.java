package com.achur.avalon.storage.inject;

import com.achur.avalon.storage.GameStore;
import com.achur.avalon.storage.GameStoreImpl;
import com.achur.avalon.storage.PlayerStore;
import com.achur.avalon.storage.PlayerStoreImpl;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;

/**
 * Avalon store module. Binds storage implementations.
 */
public class AvalonStoreModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(GameStore.class).to(GameStoreImpl.class).in(Singleton.class);
    bind(PlayerStore.class).to(PlayerStoreImpl.class).in(Singleton.class);
  }
}
