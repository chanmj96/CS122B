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
	var count = document.getElementById("input-"+text).value;
	
	if(!setCookie(text,count))
		alert("Enter a number of Movies.");
	showResult();
}

function cart_remove(elem) {
	var text = elem.value;
	eraseCookie(text);
	showResult();
}


function showResult(){
	console.log("Handling search result.");	
	
	$("#checkout").show();
	$("#search-result").show();
	$("#search_result_table").show();
	$("#search-result").css('display', 'inline-block');
	$("#search-result_table").css('display', 'inline-block');
	
	// Show Table Header
	jQuery("#search_result_head").css('display', 'table-header-group');
	
	// Populate table
	$("#search_result_body").empty();
	var element_body = jQuery("#search_result_body");
	
	var cookies = cookiesToJSON();
	for(var id in cookies){
		if(cookies[id]==null || cookies[id]=="")
			continue;
		var row = "";
		row += "<tr>";
		
		row += "<th>"+id+"</th>";
		row += "<th>"+cookies[id]+"</th>";
		

		//var count_text = "";
		//for(var num = 0; num < 10; num++)
		//	count_text += '<option value="'+num+'">'+num+'</option>';
		
		row += "<th>";
		//row += '<select id=selector-'+id+'>'+count_text+"</select>";
		row += '<input type="text" value='+cookies[id]+' name="count_input" id=input-'+id+'><br>';
		row += '<br><button onclick="update(this)" class="update" value="'+id+'">Update Item</button>';
		row += "</th>";
		
		row += "<th>";
		row += '<button onclick="cart_remove(this)" class="cart_remove" value="'+id+'">REMOVE FROM CART</button>';
		row += "</th>";
		row += "</tr>";
		element_body.append(row);
		$("#selector-"+id).val(parseInt(cookies[id]));
	}
}

$("#checkout").hide();
$( document ).ready(function(){
	showResult();
});


