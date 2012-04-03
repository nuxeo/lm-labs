jQuery(document).ready(function(){
	jQuery("#div-editTheme").dialog2({
		width : '570px',
		height : '394px',
		overflowy : 'auto',
		overflowx : 'hidden',
	    autoOpen : false,
		closeOnOverlayClick : true,
		removeOnClose : false,
		showCloseHandle : true
	  });
	
	jQuery("#modifyThemeParameters").click(function() {
	    jQuery("#div-editTheme").dialog2('open');
	    //jQuery("#div-editTheme").clearForm();
	    return false;
	  });
	
	 
	
});

function deleteBanner(url, path, msgConfirm){
	if (confirm(msgConfirm)){
		jQuery('#waitingPopup').dialog2('open');
		jQuery.ajax({
			type: "DELETE",
			url: url,
			data: '',
			success: function(msg){
				document.location.href=path + msg;
			},
			error: function(msg){
				alert( msg.responseText );
				jQuery('#waitingPopup').dialog2('close');
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

function setCallFunction(calledRef, value){
	jQuery("#valueProperty" + calledRef).val(value);
	jQuery("#spanTextAsset" + calledRef).html("(loading ...)");
}

function hideBanner(){
	jQuery("#actionMediaBanner").hide();
	if (jQuery("#bannerImgId")){
		jQuery("#bannerImgId").attr("src", jQuery("#bannerImgId").attr("src") + '?' + new Date());
	}
}

function hideLogo(){
	jQuery("#actionMediaLogo").hide();
}

function hidePropertyImage(propertyName){
	jQuery("#actionMedia" + propertyName).hide();
}

function deleteElement(url, callFunction, msgConfirm){
	if (confirm(msgConfirm)){
		jQuery('#waitingPopup').dialog2('open');
		jQuery.ajax({
			type: "DELETE",
			url: url,
			data: '',
			success: function(msg){
				//document.location.href=path + msg;
				eval(callFunction);
				if ($("#div_logo_area_height")){
					$("#div_logo_area_height").hide();
				}
				jQuery('#waitingPopup').dialog2('close');
			},
			error: function(msg){
				alert( msg.responseText );
				jQuery('#waitingPopup').dialog2('close');
			}
		});
	}
}

function openAssets(url){
	popupCenter(url, (screen.width)*2/3, (screen.height)*2/3, "menubar=no,scrollbars=auto,statusbar=no");
}