function deleteSite(obj, url, msgs){
	jQuery('#waitingPopup').dialog2('open');
	jQuery.ajax({
		type: 'PUT',
	    async: false,
	    url: url,
	    success: function(data) {
	    	if (data == 'delete') {
	          jQuery(obj).closest('tr').remove();
              jQuery('ul.nav-tabs').find('li.active a span').html(jQuery('div.tab-content div.active').find('tbody tr').size());
	          jQuery('.deleted-sites').load(jQuery('#deleted-sites').data('reload-url'));
  		      jQuery('#waitingPopup').dialog2('close');
	          jQuery.pnotify({type: 'success', title: msgs.successStatus, text: msgs.successMsg});
	        }
	        else {
  		      jQuery('#waitingPopup').dialog2('close');
	          jQuery.pnotify({hide: false, title: msgs.errorStatus, text: msgs.errorMsg});
	        }
	    },
	    error: function(jqXHR, textStatus, errorThrown) {
	    	jQuery('#waitingPopup').dialog2('close');
			jQuery.pnotify({type: 'error', hide: false, title: msgs.errorStatus, text: msgs.errorMsg + ": " + errorThrown});
	    }
	});
}
function undeleteSite(url, msgs){
	jQuery('#waitingPopup').dialog2('open');
	jQuery.ajax({
		type: 'PUT',
	    async: false,
	    url: url,
	    success: function(data) {
	    	if (data == 'undelete') {
	    		refreshAllTabs();
				jQuery('.deleted-sites').load(jQuery('#deleted-sites').data('reload-url'));
				jQuery('#waitingPopup').dialog2('close');
				jQuery.pnotify({type: 'success', title: msgs.successStatus, text: msgs.successMsg});
	        }
	        else {
				jQuery('#waitingPopup').dialog2('close');
				jQuery.pnotify({hide: false, title: msgs.errorStatus, text: msgs.errorMsg});
	        }
	    },
	    error: function(jqXHR, textStatus, errorThrown) {
			jQuery('#waitingPopup').dialog2('close');
			jQuery.pnotify({type: 'error', hide: false, title: msgs.errorStatus, text: msgs.errorMsg + ": " + errorThrown});
	    }
	});
}
function deleteDefinitelySite(url, msgs){
	jQuery('#waitingPopup').dialog2('open');
	jQuery.ajax({
		type: 'DELETE',
		async: false,
		url: url,
		success: function(data) {
			jQuery('.deleted-sites').load(jQuery('#deleted-sites').data('reload-url'));
			jQuery('#waitingPopup').dialog2('close');
			jQuery.pnotify({type: 'success', title: msgs.successStatus, text: msgs.successMsg});
		},
		error: function(jqXHR, textStatus, errorThrown) {
			jQuery('#waitingPopup').dialog2('close');
			jQuery.pnotify({type: 'error', hide: false, title: msgs.errorStatus, text: msgs.errorMsg + ": " + errorThrown});
		}
	});
}
