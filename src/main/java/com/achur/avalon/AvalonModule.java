package com.achur.avalon;

import com.achur.avalon.api.inject.AvalonApiModule;
import com.achur.avalon.processors.inject.AvalonProcessorModule;
import com.achur.avalon.storage.inject.AvalonStoreModule;

import com.google.inject.AbstractModule;

/**
 * Main entry point to the Avalon app. Installs other required
 * modules.
 */
public class AvalonModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new AvalonApiModule());
    install(new AvalonProcessorModule());
    install(new AvalonStoreModule());
  }
}
