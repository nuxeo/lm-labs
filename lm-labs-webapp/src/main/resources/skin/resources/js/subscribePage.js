function updateSubscribeButtons(data, subscribe, targetType) {
    if (subscribe) {
        jQuery('.subscribeBt' + targetType).hide();
        jQuery('.unsubscribeBt' + targetType).show();
    }
    else {
        jQuery('.subscribeBt' + targetType).show();
        jQuery('.unsubscribeBt' + targetType).hide();
    }
}

function subscribeErrorCallback() {
    /*
    console.log('subscribe failed.');
    */
}

function subscribePageUrl(subscribe, subsUrl, targetType, successCallback, errorCallback) {
    jQuery.ajax({
        type: 'GET',
        async: false,
        url: subsUrl + "/@" + (subscribe ? 'subscribe' : 'unsubscribe'),
        success: function(msg){
			if (typeof successCallback == "function") {
				successCallback(msg, subscribe, targetType);
			}
			return true;
        },
        error: function(msg){
			if (typeof errorCallback == "function") {
				errorCallback(msg, subscribe, targetType);
			}
		    return false;
        }
    });
    return false;
}
