
function getBaseURL(){
	return [location.protocol, '//', location.host, location.pathname].join('');
}
function getFullURL(){
	return window.location.href;
}

function checkout(){
	window.location.replace("checkout.html");
}

function cart_remove(elem) {
	var text = elem.value;
	var url = getBaseURL();
	var params = "";
	
	if(url.indexOf("action=") == -1){params += ("action=remove&id="+text)};
	
	$.get("ShoppingCart", params);
	$.get("ShoppingCart", (data) => showResult(data)); 
}


function showResult(result){
	console.log("Handling search result.");	
	console.log(result);
	$("#search-result").show();
	$("#search_result_table").show();
	$("#search-result").css('display', 'inline-block');
	$("#search-result_table").css('display', 'inline-block');
	
	// Show Table Header
	jQuery("#search_result_head").css('display', 'table-header-group');
	
	// Populate table
	$("#search_result_body").empty();
	var element_body = jQuery("#search_result_body");
	for(var key in result){
		var row = "";
		row += "<tr>";
		
		row += "<th>"+key+"</th>";
		row += "<th>"+result[key]+"</th>";
		
		
		// Purchase button
		row += "<th>";
		row += '<button onclick="cart_remove(this)" class="cart_remove" value="'+key+'"> REMOVE FROM CART </button>';
		row += "</th>";
		row += "</tr>";
		element_body.append(row);
	}
}

$( document ).ready(function(){
	$.get("ShoppingCart", (data) => showResult(data)); 

});


