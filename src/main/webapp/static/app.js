var avalon={createpage:{}};avalon.createpage.CreateCtrl=function(a){this.location_=a};avalon.createpage.CreateCtrl.$inject=["$location"];avalon.gamepage={};avalon.gamepage.GameCtrl=function(a){this.gameId_=a.gameId;console.log("Entering game "+this.gameId_)};avalon.gamepage.GameCtrl.$inject=["$routeParams"];avalon.indexpage={};avalon.indexpage.IndexCtrl=function(a){this.location_=a};avalon.indexpage.IndexCtrl.$inject=["$location"];avalon.application={};avalon.application.routeProvider=function(a){a.when("/index/",{templateUrl:"static/templates/indexpage.html",controller:"IndexCtrl",controllerAs:"indexCtrl"}).when("/create/",{templateUrl:"static/templates/createpage.html",controller:"CreateCtrl",controllerAs:"createCtrl"}).when("/game/:gameId/",{templateUrl:"static/templates/gamepage.html",controller:"GameCtrl",controllerAs:"gameCtrl"}).otherwise({redirectTo:"/index/"})};avalon.application.routeProvider.$inject=["$routeProvider"];
avalon.application.module=angular.module("avalon.application",["ngRoute","ngSanitize"]);avalon.application.module.config(avalon.application.routeProvider);avalon.application.module.run(["$rootScope",function(a){a.CONFIG={}}]);avalon.application.module.controller("IndexCtrl",avalon.indexpage.IndexCtrl);avalon.application.module.controller("GameCtrl",avalon.gamepage.GameCtrl);avalon.application.module.controller("CreateCtrl",avalon.createpage.CreateCtrl);
