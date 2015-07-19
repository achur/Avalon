<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<html ng-app="avalon.application">

<head>
  <script src="static/lib/angular.min.js"></script>
  <script src="static/lib/angular-resource.min.js"></script>
  <script src="static/lib/angular-route.min.js"></script>
  <script src="static/lib/angular-sanitize.min.js"></script>
  <script src="static/app.js"></script>

<%
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
