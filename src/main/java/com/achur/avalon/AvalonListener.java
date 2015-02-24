package com.achur.avalon;

import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Listener that configures servlets via Guice.
 */
public class AvalonListener extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(
        // Thin wrapper to install Avalon module, which is the actual
        // module entry point to the whole app.
        new AvalonModule());
  }
}
