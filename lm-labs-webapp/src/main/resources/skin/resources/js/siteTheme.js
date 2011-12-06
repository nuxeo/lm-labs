function deleteBanner(url, path){
	jQuery.ajax({
		type: "DELETE",
		url: url,
		data: '',
		success: function(msg){
			document.location.href=path + msg;
		},
		error: function(msg){
			alert( msg.responseText );
		}
	});
}