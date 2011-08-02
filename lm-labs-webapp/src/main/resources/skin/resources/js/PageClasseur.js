jQuery(document).ready(function(){
	var link ="";
	jQuery("#div-addfile").dialog({
		dialogClass: 'dialog-addfile',
		open: function() {},
		buttons: {
			"Annuler": function() { jQuery(this).dialog("close"); },
			"Ajouter": function() {
			    jQuery("#form-addfile").ajaxSubmit({
			        type: "POST",
			        url : link,
			        success: function(data){
			        	if (data.indexOf("Upload file ok") == -1) {
			        		allert("failed: " + data);
			        	} else {
			        		alert("OK -" + data + "-");
			                window.location.reload();
			                jQuery("#div-addfile").dialog("close");
			        	}
//			        	jQuery('#fileId').attr("value","");
			        	jQuery("#form-addfile").clearForm();
			        }
			      });
			      return true;
			}
		},
		width: 400,
		modal: true,
		autoOpen: false
	});

	jQuery(".addfile").click(function() {
		link = jQuery(this).attr("href") + "/addFile";
		jQuery("#div-addfile").dialog('open');
		return false;
	});
});