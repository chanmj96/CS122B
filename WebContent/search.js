/**
 * 
 */

function handleSearchResult(result){
	console.log("Handling search result...");
	console.log(result);
	
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


function submitSearchForm(formSubmitEvent){
	console.log("Search form submitted.");
	formSubmitEvent.preventDefault();
	
	jQuery.get(
			"/CS122B/ShowSearch",
			jQuery("#search_form").serialize(),
			(resultDataString) => handleSearchResult(resultDataString));
}
jQuery("#search_form").submit((event) => submitSearchForm(event));
