<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.utils.SystemProperty" %>
<%@ page import="com.google.common.base.Charsets" %>
<%@ page import="com.google.common.io.Files" %>
<%@ page import="java.io.File" %>

<html ng-app="avalon.application">

<head>
  <script src="static/lib/angular.min.js"></script>
  <script src="static/lib/angular-resource.min.js"></script>
  <script src="static/lib/angular-route.min.js"></script>
  <script src="static/lib/angular-sanitize.min.js"></script>

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

<body>

<%
  if (user != null) {
%>

<div class="header">
  <%= user %> | <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">Sign out</a>
</div>

<div ng-view class="view"></div>

<%
  } else {
%>

<div class="header">
  <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
</div>

<div class="welcome">
  AVALON
</div>
<%
  }
%>

</body>

</html>
