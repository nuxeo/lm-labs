var externalLinksHelper = (function() {
	// private variables & methods
	return {
		// public variables & methods
		deleteExternalURL:function(deleteUrl, path, successCallback, confirmMsg) {
			if (confirm(confirmMsg)) {
				jQuery('.external-links > div.loading-image').show();
			    jQuery('.external-links > ul').hide();
				jQuery.ajax({
					type: "DELETE",
					url: deleteUrl,
					data: '',
					success: function(msg){
						if (typeof callback == "function") {
							successCallback(msg);
							jQuery('.external-links > div.loading-image').hide();
							jQuery('.external-links > ul').show();
						}
					},
					error: function(msg){
						alert( msg.responseText );
					}
				});
			}
		}
	}
})()