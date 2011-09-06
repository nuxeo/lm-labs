jQuery(document).ready(function(){
	
	var nbPictUploaded=0;
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
		link = jQuery(this).closest("form").attr("action");
		jQuery("#div-addfile").dialog('open');
		return false;
	});

	jQuery(".opendropzone").click(function() {
		jQuery('.opendropzone').show();
		jQuery('.closedropzone').hide();
		jQuery(this).hide();
		jQuery(this).closest('form').siblings('.closedropzone').show();
		closeDropzone();
		jQuery(this).closest(".row").find('.dropzoneContainer').html("<div id=\"dropzoneEmbedded\" class=\"dropzoneStyle\"><span>Déposez les fichiers ici pour les ajouter au dossier </span><span id=\"dropFolderTitle\"></span></div>");
		link = jQuery(this).closest("form").attr("action");
		jQuery("#folderPath").val(link);
		folderTitle = jQuery(this).closest('.row').find('.colFolderTitle').text();
		jQuery("#dropFolderTitle").text(folderTitle);
		dropzoneId = "#dropzoneEmbedded";
		jQuery(dropzoneId).filedrop({
			url: link + "/addFile",
			paramname: 'userfile',
			maxfiles: 50,
			maxfilesize: 5,    // max file size in MBs
			error: function(err, file) {
				switch(err) {
			    	case 'BrowserNotSupported':
						alert('browser does not support html5 drag and drop');
						break;
					case 'TooManyFiles':
//						var tooManyFilesMsg = jQuery('#tooManyFilesMsg').val();
//						tooManyFilesMsg = tooManyFilesMsg.replace('{0}',this.maxfiles);
//						alert(tooManyFilesMsg);
						break;
			    	case 'FileTooLarge':
//						var tooLargeFileMsg = jQuery('#tooLargeFileMsg').val();
//						tooLargeFileMsg = tooLargeFileMsg.replace('{0}',this.maxfilesize);
//						alert(tooLargeFileMsg);
						break;
			    	default:
			    		alert('unknown error: ' + err + ", " + file);
						break;
				}
			},
			dragOver: function() {
				// user dragging files over #dropzone
				jQuery(dropzoneId).addClass('dropzonehighlighted');
			},
			dragLeave: function() {
				// user dragging files out of #dropzone
				jQuery(dropzoneId).removeClass('dropzonehighlighted');
			},
			docOver: function() {
			//alert('docOver id ' + idDropZone);
				// user dragging files anywhere inside the browser document window
			},
			docLeave: function() {
				// user dragging files out of the browser document window
				jQuery(dropzoneId).removeClass('dropzonehighlighted');
			},
			drop: function() {
//				console.log('drop');
				// user drops file
				jQuery(dropzoneId).removeClass('dropzonehighlighted');
			},
			uploadStarted: function(i, file, len){
//				console.log('uploadStarted ' + i + ', ' + file.name + ', ' + len);
				// a file began uploading
				// i = index => 0, 1, 2, 3, 4 etc
				// file is the actual file of the index
				// len = total files user dropped
//				if(nbPictUploaded == 0) {
					//setTimeout(function() {jQuery('#waitingPopup').dialog({ modal: true });}, 100);
					//jQuery('#waitingPopup').dialog({ modal: true });
//				}
				nbPictUploaded = nbPictUploaded+1;
//				var nbPictures = parseInt(jQuery('#nbPictures').attr("value"))+1;
//				jQuery('#nbPictures').attr("value",nbPictures);
//				return false;
			},
			uploadFinished: function(i, file, response, time) {
//				console.log('uploadFinished ' + i + ', ' + file.name + ', ' + response + ', ' + time);
				// response is the data you got back from server in JSON format.
				
//				nbPictUploaded = nbPictUploaded - 1;
				//jQuery('#waitingPopup').dialog( "close" );
//				if(0 == nbPictUploaded) {
//					var nbPictures = parseInt(jQuery('#nbPictures').attr("value"));
//					jQuery('#nbPicturesLabel').text(nbPictures);
//					var rootPath = jQuery('#rootPath').val();
//					jQuery('#picturesList').load(rootPath + "/@views/picturesList", function() {
//						disposeJSHandler();
//					});
//				}
				window.location.reload();
			},
			progressUpdated: function(i, file, progress) {
//				console.log('progressUpdated ' + i + ', ' + file.name + ', ' + progress);
				// this function is used for large files and updates intermittently
				// progress is the integer value of file being uploaded percentage to completion
			},
			speedUpdated: function(i, file, speed) {
				// speed in kb/s
			},
		    afterAll: function() {
//				console.log('afterAll');
		        // runs after all files have been uploaded or otherwise dealt with
				//jQuery('#waitingPopup').dialog( "close" );
		    },
		    rename: function(name) {
		        // name in string format
		        // must return alternate name as string
		    },
		    beforeEach: function(file) {
//				console.log('beforeEach ' + file.name);
		        // file is a file object
		        // return false to cancel upload
		    }
		}); // filedrop
		return false;
	});
	
	jQuery(".closedropzone").click(function() {
		jQuery('.opendropzone').show();
		jQuery(this).hide();
		closeDropzone();
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

	jQuery(".removefolder").click(function(evt) {
		if (confirm("Etes-vous sûr de vouloir effacer le répertoire '" + jQuery(this).closest("div").find(".colFolderTitle").text() + "' ?")) {
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

	function closeDropzone() {
		jQuery('.dropzoneContainer').html("");
	}
});