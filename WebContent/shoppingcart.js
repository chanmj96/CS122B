
function getBaseURL(){
	return [location.protocol, '//', location.host, location.pathname].join('');
}
function getFullURL(){
	return window.location.href;
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

function paginate(result, params){
	var page = parseInt(params.split("page=").pop());
	var display = parseInt(params.split("display=").pop().substring(0,2));
	if(page >= Math.ceil(result / display)){
		$("#search-wrap .pagination #next").css('display', 'none');
	} else {
		$("#search-wrap .pagination #next").css('display', 'inline');
	}
	if(page <= 1){
		$("#search-wrap .pagination #previous").css('display', 'none');
	} else {
		$("#search-wrap .pagination #previous").css('display', 'inline');
	}
}

$(".pagination a").click(function(event){
    event.preventDefault();
    var tag = $(this).attr("value");
    
	var url = getFullURL();
	var page_num = parseInt(url.split("page=").pop());
    if(tag == "next"){
    		url = url.replace(/page=.+/, "page=" + (page_num + 1));
    } else if (tag == "previous"){
		url = url.replace(/page=.+/, "page=" + (page_num - 1));
    }
	window.history.pushState(null, null, url);
	
	var params = url.split("?").pop();
	$.get("ShoppingCart", params, (data) => showResult(data));
	$.get("Count", params, (data) => paginate(data, params));
});


$("#search_result_head .sort_by").click(function(event){
	event.preventDefault();
	var value = $(this).attr("value");
	var sort = $(this).attr("sort");
	var url = getFullURL();
	
	if(sort == "ASC"){
		$(this).attr("sort", "DESC");
		url = url.replace(/(?:sort)(=.*?)[^&]*/, "sort=DESC");
	} else {
		$(this).attr("sort", "ASC");
		url = url.replace(/(?:sort)(=.*?)[^&]*/, "sort=ASC");
	}
	url = url.replace(/(?:sortby)(=.*?)[^&]*/, "sortby=" + value);
	url = url.replace(/(?:page)(=.*?)[^&]*/, "page=1");
	
	window.history.pushState(null, null, url);
	
	var params = url.split("?").pop();
	$.get("ShoppingCart", params, (data) => showResult(data));
	$.get("Count", params, (data) => paginate(data, params));	
});	


$( document ).ready(function(){
	alert("Loading Movies.")
	
	$("#search-result").css('display', 'inline-block');
	$("#search-result_table").css('display', 'inline-block');
	var url = getFullURL();
	var params = "";
	if(url.indexOf("&display=") == -1){params += "&display=10"};
	if(url.indexOf("&sort=") == -1){params += "&sort="};
	if(url.indexOf("&sortby=") == -1){params += "&sortby="};
	if(url.indexOf("&page=") == -1){params += "&page=1"};
	
	url = getBaseURL() + "?" + params;
	window.history.pushState(null, null, url);
	
	$("#search-wrap .pagination").css('display', 'inline-block');
		
	$.get("ShoppingCart", params, function() {
		  alert( "success" );
	})
	  .done(function() {
	    alert( "second success" );
	  })
	  .fail(function() {
	    alert( "error" );
	  })
	  .always(function() {
	    alert( "finished" );
	});
	//$.get("Count", params, (data) => paginate(data, params));
});


/*
$( document ).ready(function(){
	$("#search_result_table").show();
	var url = getFullURL();
	var params = "";
	if(url.indexOf("display=") == -1){params += "display=10"};
	if(url.indexOf("&sort=") == -1){params += "&sort="};
	if(url.indexOf("&sortby=") == -1){params += "&sortby=title"};
	if(url.indexOf("&page=") == -1){params += "&page=1"};
	
	url = getBaseURL() + "?" + params;
	window.history.pushState(null, null, url);
	
	$("#search-wrap .pagination").css('display', 'inline-block');
		
	$.get("ShoppingCart", params, (data) => showResult(data));
	$.get("Count", params, (data) => paginate(data, params));
}); */



