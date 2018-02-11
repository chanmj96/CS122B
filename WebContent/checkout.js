function quit(){window.location.replace("index.html");}

function onSuccess(){ 
	
	$("#checkout-form").replaceWith("<h3>Success. Thank you for your purchase!</h3>");
	$("#back").hide();
	$("#checkout").hide();
	$("#verification_error_message").hide();
	setTimeout(function(){window.location.replace("index.html")}, 3000); 
}


function handleResponse(resultDataJson) {

	// if login success, redirect to index.html page
	if (resultDataJson["status"] == "success") {
		onSuccess();
	} else {
		//console.log("show error message");
		console.log(resultDataJson["message"]);
		jQuery("#verification_error_message").text(resultDataJson["message"]);
	}
}


function verify(formSubmitEvent) {
	console.log("submit login form");
	formSubmitEvent.preventDefault();
	$.post("/CS122B/Verify", $("#checkout-form").serialize(),
		(resultDataJson) => handleResponse(resultDataJson));
}

// bind the submit action of the form to a handler function
jQuery("#checkout-form").submit((event) => verify(event));

