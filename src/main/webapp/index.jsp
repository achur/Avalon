<%@ page import="com.achur.avalon.api.Constants" %>
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

  <script>
    window.config = {};
    window.config.AUTH = {
      client_id: "<%= Constants.CLIENT_ID %>",
      scopes: ["<%= Constants.EMAIL_SCOPE %>", "<%= Constants.PROFILE_SCOPE %>"],
      immediate: false
    };
  </script>

<%
  boolean isProd =
      SystemProperty.environment.value() == SystemProperty.Environment.Value.Production;
  if (isProd) {
%>
  <script>

  </script>

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

%>
  <script src="https://apis.google.com/js/client.js?onload=init"></script>

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
          Username or something
        </h2>
      </div>
    </md-toolbar>
  </md-content>

  <div ng-view class="view"></div>


</body>

</html>
