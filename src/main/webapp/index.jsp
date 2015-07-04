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
</head>

<body>
Welcome to Avalon!

<%
  UserService userService = UserServiceFactory.getUserService();
  User user = userService.getCurrentUser();
  if (user != null) {
%>

<div class="greeting">
  User = <%= user %>
</div>

<%
  } else {
%>
<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
<%
  }
%>

<div ng-view class="view"></div>

</body>

</html>
