function getBaseURL(){
	return [location.protocol, '//', location.host, location.pathname].join('');
}
function getFullURL(){
	return window.location.href;
}
function cart(){
	window.location.replace("shoppingcart.html");
}

function back(){
	window.location.replace("index.html");
}
function showResult(result){
	console.log("Handling search result.");
	// Hide Search Functionality
	jQuery("#search_form").hide();
	
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

		// Movie Genres
		row += "<th>";
		for(var j = 0; j < result[i]["genres"].length; ++j){
			row += result[i]["genres"][j] + "<br>";
		}
		row += "</th>"
		
		// Movie Cast
		row += "<th>";
		for(var j = 0; j < result[i]["cast"].length; ++j){
			var cast_list = result[i]["cast"][j].split(",");
			var row_str = "";
			for(var k = 0; k < cast_list.length; ++k){
				var person = cast_list[k].split(':');
				row_str += "<a class=\"castmember\" value=\"" + person[1]
						+ "\" href=\"#\">" + person[0] + "</a><br>";
			}
			row += row_str + "<br>";
		}
		row += "</th>"; 
		
		// Purchase buttons
		row += "<th>";
		row += '<input type="text" value="1" id=input-'+result[i]["id"]+'><br>';
		row += '<br><button onclick="cart_add(this)" class="cart_add" value="'+result[i]["id"]+'"> ADD TO CART </button>';
		row += "</th>";
		
		row += "</tr>";
		element_body.append(row);
	}
	
	$("#search_result_body a.title").click(function(event){
		event.preventDefault();
		window.location.href = "movie.html?id=" + $(this).attr("value");
	});	
	$("#search_result_body a.castmember").click(function(event){
		event.preventDefault();
		window.location.href = "star.html?starid=" + $(this).attr("value");
	});
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
	console.log(result);
	console.log(page);
	console.log(display);
}
function submitSearchForm(formSubmitEvent){
	console.log("Search form submitted.");
	formSubmitEvent.preventDefault();

	var params = $("#search_form").serialize();
	params += "&sort=";
	params += "&sortby=";
	params += "&page=1";
	
	// Attempt at supporting browser back function;
	var new_url = [location.protocol, '//', location.host, location.pathname].join('');
	new_url += "?" + params;
	window.history.pushState(null, null, new_url);

	$("#search-wrap .pagination").css('display', 'inline-block');

	$.get("ShowSearch", params, (data)=>showResult(data));
	$.get("Count", params, (data)=>paginate(data, params));
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
	$.get("ShowSearch", params, (data) => showResult(data));
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
	$.get("ShowSearch", params, (data) => showResult(data));
	$.get("Count", params, (data) => paginate(data, params));	
});	


function cart_add(elem) {
	var text = elem.value;
	var count = document.getElementById("input-"+text).value;
	
	if(setCookie(text,count))
		alert("Cart has been updated with Movie " + text + ".");
	else
		alert("Enter a valid number of Movies.");
}

if(window.location.href.indexOf("?") != -1){
	var url = getFullURL();
	var params = url.split("?").pop();

	var useFT = false;
	if(params.split("&").length <= 1){
		useFT = true;
	}
	
	if(url.indexOf("&display=") == -1){params += "&display=10"};
	if(url.indexOf("&sort=") == -1){params += "&sort="};
	if(url.indexOf("&sortby=") == -1){params += "&sortby="};
	if(url.indexOf("&page=") == -1){params += "&page=1"};
	
	url = getBaseURL() + "?" + params;
	window.history.pushState(null, null, url);
	
	$("#search-wrap .pagination").css('display', 'inline-block');
	
	if(useFT){
		console.log("Using FullText on Normal Search...");
		$.get("ModifiedShowSearch", params, (data) => showResult(data));

	} else {
		console.log("Using EQUALITY and LIKE in Normal Search...")
		$.get("ShowSearch", params, (data) => showResult(data));
	}
	$.get("Count", params, (data) => paginate(data, params));
} else {
	$("#search_form").submit((event) => submitSearchForm(event));
}