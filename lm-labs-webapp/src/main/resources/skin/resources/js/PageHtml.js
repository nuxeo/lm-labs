$(document).ready(function() {

	prettyPrint();

	$(".dialog2").each(function() {
		$(this).dialog2({
			showCloseHandle : false,
			removeOnClose : true,
			autoOpen : false
		});
	});

	$(".open-dialog").click(function(event) {
		event.preventDefault();
		dialogId = $(this).attr("dialog");
		$(dialogId).dialog2("open");
	});

});