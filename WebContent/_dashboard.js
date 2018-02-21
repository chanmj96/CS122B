/**
 * Browse functions: get movie genres, 
 */


function getBaseURL(){
	return [location.protocol, '//', location.host, location.pathname].join('');
}
function getFullURL(){
	return window.location.href;
}
