function deleteNews(url, path){
	jQuery('#waitingPopup').dialog2('open');
	jQuery.ajax({
		type: "DELETE",
		url: url,
		data: '',
		success: function(msg){
			document.location.href=path;
		},
		error: function(msg){
			alert( msg.responseText );
			jQuery('#waitingPopup').dialog2('close');
		}
	});
}


var isOpen = false;
function openNews(id, skinBase){
	jQuery("#contentNews" + id).slideDown("slow");
	jQuery("#summaryNews" + id).hide();
	isOpen = true;
}

function closeNews(id, skinBase){
	jQuery("#contentNews" + id).hide();
	jQuery("#summaryNews" + id).show();
	isOpen = false;
}

function actionNews(id, skinBase){
	if(isOpen == false){
		openNews(id, skinBase);
	}
	else{
		closeNews(id, skinBase);
	}
}
