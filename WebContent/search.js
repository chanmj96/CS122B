function getBaseURL(){
	return [location.protocol, '//', location.host, location.pathname].join('');
}
function getFullURL(){
	return window.location.href;
}

function showResult(result, params){
	console.log("Handling search result.");	
	
	// Hide Search Functionality
	jQuery("#search_form").hide();
	
	// Create table column titles
	var element_head = jQuery("#search_result_head");
	var head = "";
	head += "<tr>";
	head += "<th>Title</th>";
	head += "<th>Release Date</th>";
	head += "<th>Director</th>";
	head += "<th>Genres</th>";
	head += "<th>Cast</th>";
	head += "</th>";
	element_head.append(head);
	
	// Populate table
	var element_body = jQuery("#search_result_body");
	for(var i = 0; i < result.length; ++i){
		var row = "";
		row += "<tr>";
		row += "<th>" + result[i]["title"] + "</th>";
		row += "<th>" + result[i]["year"] + "</th>";
		row += "<th>" + result[i]["director"] + "</th>";

		row += "<th>";
		for(var j = 0; j < result[i]["genres"].length; ++j){
			row += result[i]["genres"][j] + "<br>";
		}
		row += "</th>"
		
		row += "<th>";
		for(var j = 0; j < result[i]["cast"].length; ++j){
			row += result[i]["cast"][j] + "<br>";
		}
		row += "</th></tr>"
		element_body.append(row);
	}
	
	$("#search-wrap .pagination").css('display', 'inline-block');
	$("#search-wrap .pagination #next").css('display', 'inline');
}
function nextResult(result, params){
	$("#search_result_body").empty();
	var element_body = $("#search_result_body");
	for(var i = 0; i < result.length; ++i){
		var row = "";
		row += "<tr>";
		row += "<th>" + result[i]["title"] + "</th>";
		row += "<th>" + result[i]["year"] + "</th>";
		row += "<th>" + result[i]["director"] + "</th>";

		row += "<th>";
		for(var j = 0; j < result[i]["genres"].length; ++j){
			row += result[i]["genres"][j] + "<br>";
		}
		row += "</th>"
		
		row += "<th>";
		for(var j = 0; j < result[i]["cast"].length; ++j){
			row += result[i]["cast"][j] + "<br>";
		}
		row += "</th></tr>"
		element_body.append(row);
	}
}
function paginate(result){
	//var pagination = jQuery(".pagination");
	//var display = 10;
	
	// Show pagination 


	
	//var element_pagination = jQuery(".pagination");
	//var page = 1;
	//var link = "";
	
	/*
	if(page == 1){
		//link += "<a href=\"#\">Next</a>";
		$("#search-wrap .pagination #next").css('display', 'inline');
	} else {
		link += "<a href=\"#\">Previous</a>";
		if(page < Math.ceil(result / display)){
			link += "<a href=\"#\">Next</a>";
		}
	}
	*///old stuff, will delete
	//element_pagination.append(link);
}
function submitSearchForm(formSubmitEvent){
	console.log("Search form submitted.");
	formSubmitEvent.preventDefault();

	var params = jQuery("#search_form").serialize();
	params += "&display=10"; // Need to eventually allow user to specify
	params += "&page=1";
	// look into HTML select tag
	
	// Attempt at supporting browser back function;
	var new_url = [location.protocol, '//', location.host, location.pathname].join('');
	new_url += "?" + params;
	window.history.pushState(null, null, new_url);
	
	jQuery.get("ShowSearch", params, (data)=>showResult(data, params));
}

jQuery("#search_form").submit((event) => submitSearchForm(event));

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
	$.get("ShowSearch", params, (data) => nextResult(data, params));

    
    //link += "<a href=\"search.html?" + params + "&page=" + page-1 + "\">Previous</a><";
	//link += "<a href=\"search.html?" + params + "&page=" + page+1 + "\">Next</a>";
});