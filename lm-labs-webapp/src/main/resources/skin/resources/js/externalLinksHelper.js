var externalLinksHelper = (function() {
	// private variables & methods
	return {
		// public variables & methods
		deleteExternalURL:function(deleteUrl, successCallback, errorCallback, completeCallback) {
			jQuery.ajax({
				type: "DELETE",
				url: deleteUrl,
				data: '',
				success: function(msg){
					if (typeof successCallback == "function") {
						successCallback(msg);
					}
				},
				error: function(xhr){
					if (typeof errorCallback == "function") {
						errorCallback(xhr);
					}
				},
				complete: function(xhr) {
					if (typeof completeCallback == "function") {
						completeCallback(xhr);
					}
				}
			});
			return false;
		},
		ajaxMoveExtURL:function(url, successCallback, errorCallback, completeCallback) {
			jQuery.ajax({
				type: "GET",
				url: url,
				data: '',
				success: function(msg) {
					if (typeof successCallback == "function") {
						successCallback(msg);
					}
				},
				error: function(msg) {
					if (typeof errorCallback == "function") {
						errorCallback(msg);
					}
				},
				complete: function(xhr) {
					if (typeof completeCallback == "function") {
						completeCallback(msg);
					}
				}
			});
		}
	}
})()