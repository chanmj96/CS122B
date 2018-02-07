function makePage(result){
	console.log("Generating information...");
	
	// Add Star Name
	$("#star-name").empty();
	$("#star-name").append("<h2>" + result["name"] + "</h2>");
	
	// Add Birth Year
	$("#star-dob").empty();
	$("#star-dob").append("Date of Birth: ");
	$("#star-dob").append(result["dob"] == null ? "Unknown": result["dob"]);
	
	// Add List of Movies
	var movielist_body = $("#star-movielist");
	var mlist = result["movies"].split("----");
	console.log(result["movies"]);
	row = "";
	for(var i = 0; i < mlist.length; ++i){
		var movie = mlist[i].split("~:~");
		console.log(movie[0]);
		row += "<a class=\"title\" value=\"" + movie[1]
		+ "\" href=\"#\">" + movie[0] + "</a><br>";
	}
	movielist_body.append(row)
	
	$("#star-movielist a.title").click(function(event){
		event.preventDefault();
		window.location.href = "movie.html?id=" + $(this).attr("value");
	});
}

window.onload = function(){
	url = window.location.href;
	var param = url.split("?").pop();
	$.get("ShowStar", param, (data) => makePage(data));	
}