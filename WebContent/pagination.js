/**
 * 
 */
$("ul.pagination li a").click(function(e){
    e.preventDefault();
    var tag = $(this);
    alert(" click on "+tag.text());
    console.log("This is the pagination file");
    console.log(result);
});