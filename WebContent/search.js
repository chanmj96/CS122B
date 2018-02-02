/**
 * 
 */

function handleSearchResult(result){
	console.log("Handling search result...");
	console.log(result);
	
	var element = jQuery("#search_result_body");
	for(var i = 0; i < result.length; ++i){
		var row = "";
		row += "<tr>";
		row += "<th>" + result[i]["id"] + "</th>";
		row += "<th>" + result[i]["year"] + "</th>";
		row += "<th>" + result[i]["director"] + "</th>";
		row += "<th>" + result[i]["cast"] + "</th>";
		row += "</tr>";
		element.append(row);
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
