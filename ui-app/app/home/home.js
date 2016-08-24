(function () {
	'use strict';

	angular.module('searchdemo.home', ['ngRoute', 'ngMaterial'])
	.config(['$routeProvider', function($routeProvider) {
	  $routeProvider.when('/home', {
	    templateUrl: 'home/home.html',
	    controller: 'HomeCtrl'
	  });
	}]).controller('HomeCtrl', HomeCtrl);

	function HomeCtrl ($timeout, $q, $log, $http) { 
	 	var self = this; 
	    self.isDisabled    = false; 
	    self.matches   = fetchMatches2;
	    self.searchResults   = [];
	    self.selectedItemChange = selectedItemChange;
	    self.searchTextChange   = searchTextChange;
	    self.fetchMatches2   = fetchMatches2;
	    self.searchHotels = searchHotels;
	    self.showResultSection = true;
	    // ******************************
	    // Internal methods
	    // ******************************
	    function searchTextChange(prefix) {
	    // 	 self.matches.length = 0;
	    // 	 var url =  "http://localhost:9090/autocomplete/" + prefix +"?callback=JSON_CALLBACK"
 			 // return $http.jsonp(url).success(function(data){
 			 // 	return data.matches;
 			 // });
	    }

	    function fetchMatches2(prefix) { 
	    	 var deferred = $q.defer()
	    	 var url =  "http://localhost:9090/api1/autocomplete/" + prefix +"?callback=JSON_CALLBACK"
 			 $http.jsonp(url).success(function(data){ 
 			 	deferred.resolve(data.matches);

 			 });
 			 return deferred.promise;
	    }

	    function searchHotels(searchText) {
	    	 var url =  "http://localhost:9090/api1/search/" +  searchText +"?callback=JSON_CALLBACK"
	    	 self.searchResults.length = 0;
 			 $http.jsonp(url).success(function(data){
 			 	angular.forEach(data, function(value) {
				  this.push(value); 
				}, self.searchResults ); 
				self.showResultSection = true;
		      });  
	    }
	    function selectedItemChange(item) {
	      //$log.info('Item changed to ' + JSON.stringify(item));
	    }
	     
	     
	}
})();