
function getBaseURL(){
	return [location.protocol, '//', location.host, location.pathname].join('');
}
function getFullURL(){
	return window.location.href;
}

function checkout(){
	window.location.replace("checkout.html");
}

function cart_remove(elem) {
	var text = elem.value;
	var url = getBaseURL();
	var params = "";
	
	if(url.indexOf("action=") == -1){params += ("action=remove")};
	if(url.indexOf("&id=") == -1){params += ("&id="+text)};
	
	if(url.indexOf("&display=") == -1){params += "&display=10"};
	if(url.indexOf("&sort=") == -1){params += "&sort=ASC"};
	if(url.indexOf("&sortby=") == -1){params += "&sortby=title"};
	if(url.indexOf("&page=") == -1){params += "&page=1"};
	
	url += ("?" + params);
	window.history.pushState(null, null, url);
	
	$("#search-wrap .pagination").css('display', 'inline-block');
		
	$.post("ShoppingCart", params, (data) => showResult(data));
	//$.post("Count", params, (data) => paginate(data, params));
}


function showResult(result){
	console.log("Handling search result.");	
	console.log(result);
	$("#search_result_table").show();
	$(".pagination").show();
	// Show Table Header
	jQuery("#search_result_head").css('display', 'table-header-group');
	
	// Populate table
	$("#search_result_body").empty();
	var element_body = jQuery("#search_result_body");
	for(var i = 0; i < result.length; ++i){
		var row = "";
		row += "<tr>";
		
		// Movie Title
		row += "<th><a class=\"title\" value=\"" + result[i]["id"] 
			+ "\" href=\"#\">" + result[i]["title"] + "</a></th>";
		
		// Movie Year
		row += "<th>" + result[i]["year"] + "</th>";
		
		// Movie Director
		row += "<th>" + result[i]["director"] + "</th>";

		
		// Purchase button
		row += "<th>";
		row += '<button onclick="cart_remove(this)" class="cart_remove" value="'+result[i]["id"]+'"> REMOVE FROM CART </button>';
		row += "</th>";
		row += "</tr>";
		element_body.append(row);
	}
}

$( document ).ready(function(){
	
	$("#search-result").css('display', 'inline-block');
	$("#search-result_table").css('display', 'inline-block');
	
	$("#search-wrap .pagination").css('display', 'inline-block');
		
	$.get("ShoppingCart", function(data, status) {
		  alert( "success" );
	})
	  .done(function() {
	    alert( "second success" );
	  })
	  .fail(function(data,status) {
	    alert( status );
	  });
	//$.get("Count", params, (data) => paginate(data, params));
});


