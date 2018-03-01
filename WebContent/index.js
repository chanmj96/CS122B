/*
 * CS 122B Project 4. Autocomplete Example.
 * 
 * This Javascript code uses this library: https://github.com/devbridge/jQuery-Autocomplete
 * 
 * This example implements the basic features of the autocomplete search, features that are 
 *   not implemented are mostly marked as "TODO" in the codebase as a suggestion of how to implement them.
 * 
 * To read this code, start from the line "$('#autocomplete').autocomplete" and follow the callback functions.
 * 
 */


/*
 * This function is called by the library when it needs to lookup a query.
 * 
 * The parameter query is the query string.
 * The doneCallback is a callback function provided by the library, after you get the
 *   suggestion list from AJAX, you need to call this function to let the library know.
 */

var cache = [];
function handleLookup(query, doneCallback) {
	console.log("autocomplete initiated")
	
	// TODO: if you want to check past query results first, you can do it here
	//console.log(cache);
	for(var i = 0; i < cache.length; i++)
	{
		if(cache[i]["query"] == query)
		{
			doneCallback( {suggestions: cache[i]["data"]}  );
			//console.log("got cache results");
			return;
		}
	}
	
	
	console.log("sending AJAX request to backend Java Servlet")
	// sending the HTTP GET request to the Java Servlet endpoint hero-suggestion
	// with the query data
	/*[
    * 	{"query": query, "data": [{ "value": "Superman", "data": { "category": "dc", "heroID": 101 } }]},
    * 	{"query": query, "data": [{ "value": "Supergirl", "data": { "category": "dc", "heroID": 113 } }]}
    * ]
    */
	jQuery.ajax({
		"method": "GET",
		
		// generate the request url from the query.
		// escape the query string to avoid errors caused by special characters 
		"url": "Search?query=" + escape(query),
		"success": function(data) {
			
			// pass the data, query, and doneCallback function into the success handler
			handleLookupAjaxSuccess(data, query, doneCallback) 
		},
		"error": function(errorData) {
			console.log("lookup ajax error")
			console.log(errorData)
		}
	})
}


/*
 * This function is used to handle the ajax success callback function.
 * It is called by our own code upon the success of the AJAX request
 * 
 * data is the JSON data string you get from your Java Servlet
 * 
 */
function handleLookupAjaxSuccess(data, query, doneCallback) {
	console.log("lookup ajax successful")
	//console.log(data);
	// parse the string into JSON
	//var jsonData = JSON.parse(data);
	//console.log(jsonData)
	
	if(data == null && data.length() == 0)
		return
		
	for(var i = 0; i < cache.length; i++)
		if(cache[i]["query"] == query)
		{
			doneCallback( {suggestions: data}  );
			return;
		}
	cache.push({"query": query, "data": data});
	
	
	// call the callback function provided by the autocomplete library
	// add "{suggestions: jsonData}" to satisfy the library response format according to
	//   the "Response Format" section in documentation
	doneCallback( {suggestions: data}  );
}


/*
 * This function is the select suggestion hanlder function. 
 * When a suggestion is selected, this function is called by the library.
 * 
 * You can redirect to the page you want using the suggestion data.
 */
function handleSelectSuggestion(suggestion) {
	// TODO: jump to the specific result page based on the selected suggestion

	console.log("you selected " + suggestion["value"])
	//var url = suggestion["data"]["category"] + "-hero" + "?id=" + suggestion["data"]["heroID"]
	var url = "";
	url += window.location.protocol + "//";
	url += window.location.hostname + ":"
	url += window.location.port + "/CS122B/";
	
	if(suggestion["data"]["category"] == "movie")
		url += "movie.html?id=";
	else
		url += "star.html?starid=";
	
	url += encodeURI(suggestion["data"]["id"]);
	window.location.href = url;
	//console.log(suggestion);
	//console.log(url);
}


/*
 * This statement binds the autocomplete library with the input box element and 
 *   sets necessary parameters of the library.
 * 
 * The library documentation can be find here: 
 *   https://github.com/devbridge/jQuery-Autocomplete
 *   https://www.devbridge.com/sourcery/components/jquery-autocomplete/
 * 
 */
// $('#autocomplete') is to find element by the ID "autocomplete"
$('#autocomplete').autocomplete({
	// documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (query, doneCallback) {
    		handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
    		handleSelectSuggestion(suggestion)
    },
    // set the groupby name in the response json data field
    groupBy: "category",
    // set delay time
    deferRequestBy: 300,
    // there are some other parameters that you might want to use to satisfy all the requirements
    // TODO: add other parameters, such as mininum characters
    minChars: 3,
    autoSelectFirst: true,
    showNoSuggestionNotice: true,
    lookupLimit: 10
});


/*
 * do normal full text search if no suggestion is selected 
 */
function handleNormalSearch(query) {
	console.log("doing normal search with query: " + query);
	// TODO: you should do normal search here
	var url = "";
	url += window.location.protocol + "//";
	url += window.location.hostname + ":"
	url += window.location.port + "/CS122B/search.html?title=";
	url += query;
	window.location.href = url;

}

// bind pressing enter key to a hanlder function
$('#autocomplete').keypress(function(event) {
	// keyCode 13 is the enter key
	if (event.keyCode == 13) {
		// pass the value of the input box to the hanlder function
		handleNormalSearch($('#autocomplete').val())
	}
});

//TODO: if you have a "search" button, you may want to bind the onClick event as well of that button
$('#fsearch #fbutton').click(function(event) {
	console.log("Submit button pressed.");
	event.preventDefault();
	handleNormalSearch($('#autocomplete').val());
});
$('#main-wrap .btn').click(function(event) {
	console.log("Browse / Search / Cart button pressed.");
	event.preventDefault();
	var url = "";
	url += window.location.protocol + "//";
	url += window.location.hostname + ":";
	url += window.location.port + "/CS122B/";
	url += $(this).attr('value');
	window.location.href = url;
});
$('#cart').click(function(event){
	console.log("Cart button pressed.");
	event.preventDefault();
	var url = "";
	url += window.location.protocol + "//";
	url += window.location.hostname + ":";
	url += window.location.port + "/CS122B/";
	url += "shoppingcart.html";
	window.location.href = url;
});