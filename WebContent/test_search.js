/**
 * 
 */
	$('.live-search-list').hide();

function dosomething(event){
	console.log("Submitted test event.");
	event.preventDefault();
	$.get("Search", $("#search_form").serialize());
}
function list(result){
	console.log("Creating list...");
	$("#live-search ul").empty()
	var listings = $("#live-search ul");
	
	for(var i = 0; i < Math.min(result.length, 10); ++i){
		var id = result[i]['id'];
		var title = result[i]['title'];
		console.log(id + ": " + title);
		listings.append(
			"<li data-search-term='" + title 
			+ "' value='" + id 
			+ "' ><a href='" + id
			+ "' >"+ title 
			+ "</a></li>")
	}
	console.log("---------");
	
	
	if(result == 0){
		$(".live-search-list").hide();
	} else {
		$(".live-search-list").show();
	}
}

$("#search_form").submit((event) => dosomething(event));

$(document).ready(function($){
	$('.live-search-box').on('keyup', function(){
		var search = $(this).val();
		$.get("Search", "input=" + search, (event) => list(event));
	});
});