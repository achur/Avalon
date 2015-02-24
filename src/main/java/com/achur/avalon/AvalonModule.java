package com.achur.avalon;

import com.achur.avalon.api.inject.AvalonApiModule;

import com.google.inject.AbstractModule;

/**
 * Main entry point to the Avalon app. Installs other required
 * modules.
 */
public class AvalonModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new AvalonApiModule());
  }
}
