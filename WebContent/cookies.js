
function getCookie(name)
{
	var paramName = name + "=";
	var decodedCookie = decodeURIComponent(document.cookie);
	var split = decodedCookie.split(';');
	for(var i=0; i<split.length; ++i)
	{
		var c = split[i];
		while(c.charAt(0)==' ') c = c.substring(1,c.length);
		if (c.indexOf(paramName) == 0) return c.substring(paramName.length, c.length);
	}
	return "";
}

function setCookie(name,value)
{
	if(isNaN(parseInt(value)))
		return false;
	if(value <= 0)
	{	eraseCookie(name);
		return true;
	}
	console.log("setting cookie " + name + "=" + parseInt(value).toString());
	document.cookie = name + "=" + (parseInt(value).toString()) +"; path=/;";
	return true;
}

function eraseCookie(name) {   
    document.cookie = name+"=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/"
}

function eraseAllCookies(name) { 
	var str = decodeURIComponent(document.cookie);
	console.log(decodeURIComponent(document.cookie));
    str = str.split(', ');
    var result = {};
    for (var i = 0; i < str.length; i++) {
        var cur = str[i].split('=');
		document.cookie = cur[0]+"=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/"
	}
}

function cookiesToJSON(){
	var str = decodeURIComponent(document.cookie);
	console.log(decodeURIComponent(document.cookie));
    str = str.split('; ');
    var result = {};
    for (var i = 0; i < str.length; i++) {
        var cur = str[i].split('=');
        result[cur[0]] = cur[1];
    }
    console.log("cookiesToJson");
    console.log(result);
    return result;
}
