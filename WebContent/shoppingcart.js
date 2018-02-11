
function getBaseURL(){
	return [location.protocol, '//', location.host, location.pathname].join('');
}
function getFullURL(){
	return window.location.href;
}

function checkout(){
	window.location.replace("checkout.html");
}
function back(){
	window.location.replace("index.html");
}
function update(elem){
	var text = elem.value;
	var url = getBaseURL();
	var params = "";
	var count = $('#selector-'+text).find(":selected").text();
	
	params += ("action=update");
	params += ("&id="+text);
	params += ("&count="+count);
	
	url += ("?" + params);
	window.history.pushState(null, null, url);
	
	console.log(url);
	$.get("ShoppingCart", params, function(data,status){});
	$.get("ShoppingCart", (data) => showResult(data)); 
}

function cart_remove(elem) {
	var text = elem.value;
	var url = getBaseURL();
	var params = "";

	params += ("action=remove");
	params += ("&id="+text);
	params += ("&count=100");
	
	url += ("?" + params);
	window.history.pushState(null, null, url);
	
	console.log(url);
	$.get("ShoppingCart", params, function(data,status){});
	$.get("ShoppingCart", (data) => showResult(data)); 
}


function showResult(result){
	console.log("Handling search result.");	
	if(result.length > 0)
		$("#checkout").show();
	else
		$("#checkout").hide();
	$("#search-result").show();
	$("#search_result_table").show();
	$("#search-result").css('display', 'inline-block');
	$("#search-result_table").css('display', 'inline-block');
	
	// Show Table Header
	jQuery("#search_result_head").css('display', 'table-header-group');
	
	// Populate table
	$("#search_result_body").empty();
	var element_body = jQuery("#search_result_body");
	for(var i=0; i<result.length; ++i){
		var row = "";
		row += "<tr>";
		
		row += "<th>"+result[i]["id"]+"</th>";
		row += "<th>"+result[i]["name"]+"</th>";
		row += "<th>"+result[i]["quantity"]+"</th>";
		

		var count_text = "";
		for(var num = 0; num < 10; num++)
			count_text += '<option value="'+num+'">'+num+'</option>';
		
		row += "<th>";
		row += '<select id=selector-'+result[i]["id"]+'>'+count_text+"</select>";
		row += '<br><button onclick="update(this)" class="update" value="'+result[i]["id"]+'">Update Item</button>';
		row += "</th>";
		
		row += "<th>";
		row += '<button onclick="cart_remove(this)" class="cart_remove" value="'+result[i]["id"]+'">REMOVE FROM CART</button>';
		row += "</th>";
		row += "</tr>";
		element_body.append(row);
		$("#selector-"+result[i]["id"]).val(parseInt(result[i]["quantity"]));
	}
}

$("#checkout").hide();
$( document ).ready(function(){
	$.get("ShoppingCart", (data) => showResult(data)); 
});


