/**
 * 
 */
$(".pagination a").click(function(event){
    e.preventDefault();
    var tag = $(this);
    alert(" click on "+tag.text());
    console.log("This is the pagination file");
    //link += "<a href=\"search.html?" + params + "&page=" + page-1 + "\">Previous</a><";
	//link += "<a href=\"search.html?" + params + "&page=" + page+1 + "\">Next</a>";
});