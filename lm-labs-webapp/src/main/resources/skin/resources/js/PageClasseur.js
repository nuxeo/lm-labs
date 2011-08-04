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

	jQuery("#div-addfolder").dialog({
		dialogClass: 'dialog-addfolder',
		open: function() {},
		buttons: {
			"Annuler": function() { jQuery(this).dialog("close"); },
			"Ajouter": function(evt) {
				if (jQuery("#folderName").attr("value").length > 0) {
					var buttonDomElement = evt.target;
					$(buttonDomElement).attr('disabled', true);
					jQuery.ajax({
						url: jQuery('#form-addfolder').attr("action"),
						type: "POST",
						data: { folderName: jQuery("#folderName").attr("value") },
						success: function (data, textStatus, jqXHR) {
							jQuery(this).dialog("close");
							jQuery("#form-addfolder").resetForm();
							window.location.reload();
						},
						error: function(jqXHR, textStatus, errorThrown) {
							alert(errorThrown + ": " + "," + jqXHR.responseText);
							$(buttonDomElement).attr('disabled', false);
						}
					});
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

	jQuery("#addFolder").click(function() {
		jQuery("#div-addfolder").dialog('open');
		return false;
	});

	jQuery(".removefile").click(function(evt) {
		if (confirm("Etes-vous sûr de vouloir effacer le fichier '" + jQuery(this).closest("div").children(".colFileName").text() + "' ?")) {
			var buttonDomElement = evt.target;
			$(buttonDomElement).attr('disabled', true);
			jQuery.ajax({
				url: jQuery(this).closest("form").attr("action"),
				type: "DELETE",
				success: function (data, textStatus, jqXHR) {
					window.location.reload();
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert(errorThrown + ": " + "," + jqXHR.responseText);
				}
			});
		}
		return false;
	});

	jQuery("#deleteSelection").click(function(evt) {
		if (confirm("Etes-vous sûr de vouloir effacer les fichiers sélectionnés ?")) {
			var buttonDomElement = evt.target;
			jQuery(buttonDomElement).attr('disabled', true);
			var url = jQuery('.classeur').attr("id") + "/bulk?";
			var n = jQuery('input[name="checkoptions"]:checked').length;
			if (n <= 0) {
				return false;
			}
			jQuery('input[name="checkoptions"]:checked').each(function(i) {
				url += "id=" + this.value;
				if (i < n-1) {
					url += "&";
				}
			});
			jQuery.ajax({
				url: url,
				type: "DELETE",
				complete: function(jqXHR, textStatus) {
					jQuery('input[name="checkoptions"]').attr('checked', false);
				},
				success: function (data, textStatus, jqXHR) {
					window.location.reload();
				},
				error: function(jqXHR, textStatus, errorThrown) {
					jQuery(buttonDomElement).attr('disabled', false);
					alert(errorThrown + ": " + "," + jqXHR.responseText);
				}
			});
		}
		return false;
	});

});