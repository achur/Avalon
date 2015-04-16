package com.achur.avalon.api;

import com.google.api.server.spi.config.Api;


@Api(name="avalon", version="v1", description="API access to Avalon game",
     clientIds = {
       Constants.CLIENT_ID,
       com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID
     },
     scopes = {
       Constants.EMAIL_SCOPE,
       Constants.PROFILE_SCOPE
     })
public class AvalonApi {
}
