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
}

function paginate(result){
	var pagination = jQuery("#pagination");
	var perpage = 10;
	
	// Create pagination 
	$("#search-wrap .pagination").css('display', 'inline-block');

	var element_pagination = jQuery(".pagination");
	var page = 1;
	var link = "";
	if(page == 1){
		link += "<a href=\"#\">Next</a>";
	} else {
		link += "<a href=\"#\">Previous</a>";
		if(page < Math.ceil(result / perpage)){
			link += "<a href=\"#\">Next</a>";
		}
	}
	element_pagination.append(link);
}

function submitSearchForm(formSubmitEvent){
	console.log("Search form submitted.");
	formSubmitEvent.preventDefault();
	
	var params = jQuery("#search_form").serialize();
	jQuery.get("/CS122B/ShowSearch", params, (data)=>showResult(data, params));
	jQuery.get("/CS122B/Count", params, (data)=>paginate(data));
}

jQuery("#search_form").submit((event) => submitSearchForm(event));