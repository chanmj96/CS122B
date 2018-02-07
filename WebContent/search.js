function getBaseURL(){
	return [location.protocol, '//', location.host, location.pathname].join('');
}
function getFullURL(){
	return window.location.href;
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
		
		// Purchase button
		row += "<th>";
		row += "<button type=\"button\" class=\"cart\"> ADD TO CART </button>"; 
		row += "</th>";
		row += "</tr>";
		element_body.append(row);
	}
	
	
	// Work in progress
	$("#search_result_body a.title").click(function(event){
		event.preventDefault();
		window.location.href = "movie.html?id=" + $(this).attr("value");;
		
		console.log("This movie has ID: " + $(this).attr("value"));
	});
	
	// Work in progress
	$("#search_result_body a.castmember").click(function(event){
		event.preventDefault();
		console.log("This cast member has ID: " + $(this).attr("value"));
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
}
function submitSearchForm(formSubmitEvent){
	console.log("Search form submitted.");
	formSubmitEvent.preventDefault();

	var params = jQuery("#search_form").serialize();
	params += "&sort=";
	params += "&sortby=";
	params += "&page=1";
	// look into HTML select tag
	
	// Attempt at supporting browser back function;
	var new_url = [location.protocol, '//', location.host, location.pathname].join('');
	new_url += "?" + params;
	window.history.pushState(null, null, new_url);

	$("#search-wrap .pagination").css('display', 'inline-block');

	$.get("ShowSearch", params, (data)=>showResult(data));
	$.get("Count", params, (data)=>paginate(data, params));
}

$("#search_form").submit((event) => submitSearchForm(event));

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