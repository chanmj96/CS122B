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
	head += "<th>Cast</th>";
	head += "</th>";
	element_head.append(head);
	
	var element_body = jQuery("#search_result_body");
	for(var i = 0; i < result.length; ++i){
		for(var key in result[i]){
			var row = "";
			row += "<tr>";
			row += "<th>" + result[i][key]["title"] + "</th>";
			row += "<th>" + result[i][key]["year"] + "</th>";
			row += "<th>" + result[i][key]["director"] + "</th>";

			row += "<th>";
			for(var j = 0; j < result[i][key]["cast"].length; ++j){
				row += result[i][key]["cast"][j] + "<br>";
			}
			row += "</th>"
		}
		row += "</tr>";
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
