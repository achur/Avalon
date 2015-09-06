/**
 * Module for the Avalon topbar.
 */

goog.provide('avalon.topbar.module');

goog.require('avalon.topbar.TopbarDirective');
goog.require('avalon.topbar.UserLoadingDirective');

avalon.topbar.module = angular.module('avalon.topbar', []).
    directive('userLoading', avalon.topbar.UserLoadingDirective).
    directive('avalonTopbar', avalon.topbar.TopbarDirective);
