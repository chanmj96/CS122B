function back(){
	window.location.replace("index.html");
}
function cart(){
	window.location.replace("shoppingcart.html");
}

function makePage(result){
	console.log("Generating information...");
	
	// Add title
	$("#movie-title").empty();
	var title = $("#movie-title");
	title.append("<h2><b>" + result["title"] + "</h2></b>");
	
	// Add year and director
	$("#movie-info .year").empty();
	$("#movie-info .director").empty();
	$("#movie-info .genres").empty();
	$("#movie-info .cast").empty();
	
	$(".year").append(result["year"]);
	$(".director").append("Directed by: " + result["director"]);
	
	// Add hyperlinked genres
	var genres_body = $(".genres");
	var glist = result["genres"][0].split(",");
	row = "";
	for(var i = 0; i < glist.length; ++i){
		row += "<a class=\"genre\" value=\"" + glist[i]
			+ "\" href=\"#\">" + glist[i] + "</a>&nbsp";
	}
	genres_body.append(row);
	
	// Add hyperlinked star names
	var cast_body = $("#movie-info .cast");
	for(var i = 0; i < result["cast"].length; ++i){
		var row = "";
		var clist = result["cast"][i].split(",");
		for(var j = 0; j < clist.length; ++j){
			var person = clist[j].split(':');
			row += "<a class=\"castmember\" value=\"" + person[1]
			+ "\" href=\"#\">" + person[0] + "</a><br>";
		}
		cast_body.append(row);
	}
	
	$("#movie-info a.genre").click(function(event){
		event.preventDefault();
		console.log("This movie has genre: " + $(this).attr("value"));
	});
	$("#movie-info a.castmember").click(function(event){
		event.preventDefault();
		window.location.href = "star.html?starid=" + $(this).attr("value");
	});
}

window.onload = function(){
	url = window.location.href;
	var param = url.split("?").pop();
	$.get("ShowMovie", param, (data) => makePage(data));	
}