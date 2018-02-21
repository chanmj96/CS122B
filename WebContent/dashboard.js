/**
 * 
 */

function addSuccess(result){
	// Print success message if the employee was successfully added
	// Otherwise print error message
	$(".result").empty();
	if(result){
		$(".result").append("Star added successfully!");
	} else {
		$(".result").append("Unable to complete insertion of star.");
	}
}

function addStar(info){
	info.preventDefault();
	console.log("Data sent.");

	$.get("AddStar", $("#dashboard_form").serialize(), (result) => addSuccess(result)); 
}

function printMetadata(data){
	console.log(data);
	$("meta-result").append(data["tables"]);
	console.log(data["tables"]);
	$("meta-result").append(data["customers"]);
} 

$("#dashboard_form").submit( (event) => addStar(event));

$("#meta-btn").click(function() {
	$.get("ShowMetadata", (data) => printMetadata(data));
})