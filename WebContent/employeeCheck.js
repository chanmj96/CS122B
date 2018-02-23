$.get("Employee", function(data) {
	console.log(data);
	result= JSON.parse(data);
	if (result["status"] == "fail") {
		window.location.replace("index.html");
	}
	
} ); 

