package com.achur.avalon.api.inject;

import com.achur.avalon.api.GameApi;

import com.google.api.server.spi.guice.GuiceSystemServiceServletModule;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.HashSet;

/**
 * Installs all necessary packages for the Avalon API.
 */
public class AvalonApiModule extends GuiceSystemServiceServletModule {

  @Override
  protected void configureServlets() {
    super.configureServlets();

    installModules();

    this.serveGuiceSystemServiceServlet("/_ah/spi/*", Sets.newHashSet(
            GameApi.class
        ));
  }

  /**
   * Install modules needed for the Avalon API.
   */
  private void installModules() {
  }

}
