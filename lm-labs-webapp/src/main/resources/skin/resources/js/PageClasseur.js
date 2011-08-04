jQuery(document).ready(function(){
	
	jQuery("#downloadFile").click(function() {
		// TODO
	});
	
	var link ="";
	jQuery("#div-addfile").dialog({
		dialogClass: 'dialog-addfile',
		open: function() {},
		buttons: {
			"Annuler": function() { jQuery(this).dialog("close"); },
			"Ajouter": function(evt) {
				if (jQuery("#fileId").attr("value").length > 0) {
					jQuery(this).dialog("close");
					setTimeout(function() {jQuery('#waitingPopup').dialog({ modal: true });}, 100);

					jQuery("#form-addfile").ajaxSubmit({
						type: "POST",
						url : link + "/addFile",
						success: function(data){
							if (data.indexOf("Upload file ok") == -1) {
								alert("failed: " + data);
							} else {
								window.location.reload();
							}
							jQuery("#form-addfile").resetForm();
						}
					});
					$('#waitingPopup').dialog( "close" );
					return true;
				}
			}
		},
		width: 400,
		modal: true,
		autoOpen: false
	});

	jQuery(".addfile").click(function() {
		link = jQuery(this).attr("href");
		jQuery("#div-addfile").dialog('open');
		return false;
	});
});