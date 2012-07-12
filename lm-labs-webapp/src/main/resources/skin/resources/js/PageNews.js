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
function openNews(id, skinBase, iconObj) {
	jQuery(iconObj).closest('section').find('div.news-title div.for-summary').hide();
	jQuery(iconObj).closest('section').find('div.news-title div.for-content').show();
	jQuery(iconObj).closest('section').find('div.newsContent').slideDown("slow");
	isOpen = true;
}

function closeNews(id, skinBase, iconObj) {
	jQuery(iconObj).closest('section').find('div.news-title div.for-content').hide();
	jQuery(iconObj).closest('section').find('div.news-title div.for-summary').show();
	jQuery(iconObj).closest('section').find('div.newsContent').hide();
	isOpen = false;
}

function actionNews(id, skinBase, obj){
	if(isOpen == false){
		openNews(id, skinBase, obj);
	}
	else{
		closeNews(id, skinBase, obj);
	}
}
