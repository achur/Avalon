<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.utils.SystemProperty" %>
<%@ page import="com.google.common.base.Charsets" %>
<%@ page import="com.google.common.io.Files" %>
<%@ page import="java.io.File" %>

<html ng-app="avalon.application">

<head>
  <link rel="stylesheet" type="text/css" href="static/app.css">
  <link rel="stylesheet" type="text/css" href="static/lib/angular-material.min.css">
  <script src="static/lib/angular.min.js"></script>
  <script src="static/lib/angular-resource.min.js"></script>
  <script src="static/lib/angular-route.min.js"></script>
  <script src="static/lib/angular-sanitize.min.js"></script>
  <script src="static/lib/angular-animate.min.js"></script>
  <script src="static/lib/angular-aria.min.js"></script>
  <script src="static/lib/angular-material.min.js"></script>

<%
  boolean isProd =
      SystemProperty.environment.value() == SystemProperty.Environment.Value.Production;
  if (isProd) {
%>

  <script src="static/app.js"></script>

<%
  } else {
%>
  <script src="static/lib/goog/base.js"></script>
<%
    for (String filename : Files.readLines(new File("src/main/webapp/static/app.MF"), Charsets.UTF_8)) {
%>

  <script src="<%= filename %>"></script>

<%
    }
  }

  UserService userService = UserServiceFactory.getUserService();
  User user = userService.getCurrentUser();
%>

  <script>
    window.avalonConfig = {
      user: "<%= user %>"
    };
  </script>
</head>

<body layout="column">

  <md-content>
    <md-toolbar>
      <div class="md-toolbar-tools">
        <h2>
          <a class="navbar-brand" href="/#/">Avalon</a>
        </h2>
        <span flex></span>
        <h2 class="topbar-username">
<%
  if (user != null) {
%>
          <span><%= user %></span>
          <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">Sign Out</a>
<%
  } else {
%>
          <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign In</a>
<%
  }
%>
        </h2>
      </div>
    </md-toolbar>
  </md-content>

<%
  if (user != null) {
%>
  <div ng-view class="view"></div>
<%
  } else {
%>
  <div class="welcome">
    AVALON
  </div>
<%
  }
%>

</body>

</html>
