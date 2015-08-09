/**
 * Module for the request service.
 */

goog.provide('avalon.request.module');

goog.require('avalon.request.RequestService');

avalon.request.module = angular.module('avalon.request', []).
    service('requestService', avalon.request.RequestService);
