
$(document).ready(function() {

	$('[id^="listdetail_"]').colorbox({width:"80%", height:"80%"});
		
	$('[id^="opendetail_"]').click(function (event) {
		event.preventDefault(); // this just cancels the default link behavior.
		parent.showColorBox($(this).attr("href")); //this makes the parent window load the showColorBox function, using the a.colorbox href value
	});
	
});

function showColorBox(imageURL) {
	$.fn.colorbox({ innerWidth: "80%", innerHeight: "80%",  transition: "elastic", opacity: .6, open: true, href: imageURL });
}
		