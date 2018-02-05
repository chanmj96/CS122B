function parseURL(query, key){
	var params = query.split("&");
	for(var i = 0; i < params.length; ++i){
		var tag = params[i].split("=");
		if(tag[0] == key){
			return tag[1];
		}
	}
}

function handleSearchResult(result){
	console.log("Handling search result.");	
	jQuery("#functions").css("display", "none");
	
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
	pagination = pagination / 10; // Offset
	
	var head = "";
	head += "<"
}


function submitSearchForm(formSubmitEvent){
	console.log("Search form submitted.");
	formSubmitEvent.preventDefault();
	console.log(jQuery("#search_form").serialize());
	
	jQuery.get(
			"/CS122B/ShowSearch",
			jQuery("#search_form").serialize(),
			(resultDataString) => handleSearchResult(resultDataString));
	jQuery.get(
			"/CS122B/Count",
			jQuery("#search_form").serialize(),
			(result) => paginate(result));
}
jQuery("#search_form").submit((event) => submitSearchForm(event));
$("ul.pagination li a").click(function(e){
    e.preventDefault();
    var tag = $(this);
    alert(" click on "+tag.text());
});
