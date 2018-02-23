/**
 * 
 */

function addSuccess(result){
	// Print success message if the employee was successfully added
	// Otherwise print error message
	console.log(result);
	$(".result").empty();
	if(result){
		$(".result").append("<br>Updated the database successfully!");
	} else {
		$(".result").append("<br>Unable to update database.");
	}
}

function addStar(info){
	info.preventDefault();
	console.log("Data sent.");

	$.get("AddStar", $("#dashboard_form").serialize(), (result) => addSuccess(result)); 
}

function addMovie(info){
	info.preventDefault();
	console.log("Data sent.");

	$.get("AddMovie", $("#dashboard_form").serialize(), (result) => addSuccess(result)); 
}

function printMetadata(data){	
	var page = $("#dashboard-page");
	
	// Show database tables
	var tables = ""
	tables += "<div id=\"meta-tables\" class=\"meta-data\">";
	tables += "<h3> Tables </h3>";
	tables += " &middot ";
	for(var i = 0; i < data["tables"].length; ++i){
		tables += "<a href=#" + data["tables"][i].replace(/(["])/g, "") + ">" + data["tables"][i] + "</a> &middot ";
	}
	tables += "</div><br>";
	page.append(tables);
	
	// Show database columns and types per table
	for(var key in data){
		if(key != "tables"){
			var info = "";
			info += "<div id=\"" + key + "\" class=\"meta-data\">";
			info += "<h3><a name=\"" + key + "\">" + key + "</a></h3>";
			info += "<table class=\"table table-striped\"><thead>";
			info += "<tr><th>Attribute</th><th>Type</th></tr>"
			
			info += "<tbody>"
			for(var attr in data[key]){
				info += "<tr>";
				info += "<th>" + attr + "</th>";
				info += "<th>" + data[key][attr] + "</th>";
				info += "</tr>";
			}
			
			info += "</tbody></div>";
			page.append(info);	
		}
	}
} 


$("#navbar #addstar").click(function(e) {
	location.hash = "";
	history.pushState(null, "", location.href.replace("#", ""));
	
	$('li a').removeClass('active');
	$(this).addClass('active');
	e.preventDefault();
	console.log("AddStar clicked!");
	$("#dashboard-page").empty();
	$(".result").empty();
	
	var page = $("#dashboard-page");
	var data = "";
	data += "<br><br><form id=\"dashboard_form\" action=\"#\" method=\"GET\">";
	data +=	"<h3> Add Star </h3>";
	data += "<div id=\"functions\">";
	data += "<div class=\"dashboard-form\"><strong> Name </strong><input type=\"text\" name=\"name\"><br></div>";
	data += "<div class=\"dashboard-form\"><strong> Birthday </strong><input type=\"text\" name=\"dob\"><br></div>";
	data += "<br><div id=\"submit\"><input type=\"submit\" value=\"submit\"></div></form>";
	page.append(data);
	
	$("#dashboard_form").submit( (event) => addStar(event));
});

$("#navbar #addmovie").click(function(e) {
	location.hash = "";
	history.pushState(null, "", location.href.replace("#", ""));
	
	$('li a').removeClass('active');
	$(this).addClass('active');	e.preventDefault();
	console.log("AddMovie clicked!");
	$("#dashboard-page").empty();
	$(".result").empty();
	
	var page = $("#dashboard-page");
	var data = "";
	data += "<br><br><form id=\"dashboard_form\" action=\"#\" method=\"GET\">";
	data +=	"<h3> Add Movie </h3>";
	data += "<div id=\"functions\">";
	data += "<div class=\"dashboard-form\"><strong> Title </strong><input type=\"text\" name=\"title\"><br></div>";
	data += "<div class=\"dashboard-form\"><strong> Year </strong><input type=\"text\" name=\"year\"><br></div>";
	data += "<div class=\"dashboard-form\"><strong> Director </strong><input type=\"text\" name=\"director\"><br></div>";
	data += "<div class=\"dashboard-form\"><strong> Star </strong><input type=\"text\" name=\"star\"><br></div>";
	data += "<div class=\"dashboard-form\"><strong> Genre </strong><input type=\"text\" name=\"genre\"><br></div>";
	data += "<br><br><br><div id=\"submit\"><input type=\"submit\" value=\"submit\"></div></form>";
	page.append(data);
	
	$("#dashboard_form").submit( (event) => addMovie(event));
});

$("#navbar #metadata").click(function(e) {
	location.hash = "";
	history.pushState(null, "", location.href.replace("#", ""));
	
	$('li a').removeClass('active');
	$(this).addClass('active');
	e.preventDefault();
	console.log("Metadata clicked!");
	$("#dashboard-page").empty();
	$(".result").empty();

	$.get("ShowMetadata", (data) => printMetadata(data));
});