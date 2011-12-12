jQuery(document).ready(function(){
	
	jQuery("#div-editTheme").dialog2({
	    open: function() {},
	    autoOpen : false,
		closeOnOverlayClick : true,
		removeOnClose : false,
		showCloseHandle : true,
	  });
	
	jQuery("#modifyThemeParameters").click(function() {
	    jQuery("#div-editTheme").dialog2('open');
	    //jQuery("#div-editTheme").clearForm();
	    return false;
	  });
	
});

function deleteBanner(url, path, msgConfirm){
	if (confirm(msgConfirm)){
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
}

function manageDisplayModifyParameters(value){
	if (jQuery("#theme").val() == value){
		jQuery("#modifyThemeParameters").show();
	}
	else{
		jQuery("#modifyThemeParameters").hide();
	}
}