var multipleFileUploads = true;
jQuery(document).ready(function(){

	// Check for the various File API support.
	if (window.File && window.FileReader && window.FileList && window.Blob) {
	  // Great success! All the File APIs are supported.
	} else {
	  multipleFileUploads = false;
	  jQuery('input[multiple]').siblings('p.help-block').hide();
	  jQuery('form.form-upload-file').find('div.alert').show();
	  jQuery('input[multiple]').removeAttr('multiple');
	  //alert('The File APIs are not fully supported in this browser.');
	}
	
  var nbPictUploaded=0;
  var link ="";

  jQuery("#div-moveElements").dialog2({
    buttons: {
      "Annuler": function() { jQuery(this).dialog2("close"); }
    },
  	autoOpen : false
  });

  jQuery("#div-addfolder").dialog2({
    open: function() {},
    buttons: {
      "Annuler": function() { jQuery(this).dialog2("close"); }
    },
    width: '650px',
    autoOpen : false,
	closeOnOverlayClick : true,
	removeOnClose : false,
	showCloseHandle : true
  });

  jQuery("#div-renameTitleElement").dialog2({
    buttons: {
      "Annuler": function() { jQuery(this).dialog2("close"); }
    },
    width: '500px',
    autoOpen : false,
	closeOnOverlayClick : true,
	removeOnClose : false,
	showCloseHandle : true
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
      maxfilesize: 20,    // max file size in MBs
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
        //jQuery('#waitingPopup').dialog2( "close" );
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
        //jQuery('#waitingPopup').dialog2( "close" );
        },
        rename: function(name) {
            // name in string format
            // must return alternate name as string
          var escaped = name;
          return escaped;
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

  jQuery(".btn.addFolder").click(function() {
    jQuery("#div-addfolder").dialog2('open');
    jQuery("#div-addfolder").dialog2("options",
    	{
    	title: "Ajouter un répertoire"
    	}
    );
    jQuery("#folderDescription").val('');
    jQuery("#form-folder").clearForm();
	jQuery('#folderDescription').ckeditor(ckeditorconfigUser);

    return false;
  });
  
  jQuery(".removefile").click(function(evt) {
    if (confirm("Etes-vous sûr de vouloir effacer le fichier '" + jQuery(this).closest("tr").find(".colFileName").text() + "' ?")) {
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
      jQuery('#waitingPopup').dialog2('open');
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

  jQuery(".selectionActionsBt.deleteSelection").click(function(evt) {
    if (confirm("Etes-vous sûr de vouloir effacer les fichiers sélectionnés ?")) {
      jQuery('#waitingPopup').dialog2('open');
      var buttonDomElement = evt.target;
      jQuery(buttonDomElement).attr('disabled', true);
      var url = jQuery('.classeur').attr("id") + "/bulk?";
      var n = jQuery('input[name="checkoptions"]:checked').length;
      if (n <= 0) {
        return false;
      }
      url += getIdsUrlParamsString();
      jQuery.ajax({
        url: url,
        type: "DELETE",
        complete: function(jqXHR, textStatus) {
        	jQuery('#waitingPopup').dialog2('close');	
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
      jQuery('#waitingPopup').dialog2('close');
    }
    return false;
  });

  jQuery(".selectionActionsBt.moveSelection").click(function(evt) {
	  jQuery('#div-moveElements').dialog2('open');
  });

  jQuery(".selectionActionsBt.hideSelection, .selectionActionsBt.showSelection").click(function(evt) {
	  jQuery('#waitingPopup').dialog2('open');
	  var action = '';
	  if (jQuery(this).hasClass('hideSelection')) {
		  action = 'hide';
	  } else {
		  action = 'show';
	  }
	  var buttonDomElement = evt.target;
	  jQuery(buttonDomElement).attr('disabled', true);
	  var url = jQuery('.classeur').attr("id") + "/@filesVisibility/" + action + "?";
	  var n = jQuery('input[name="checkoptions"]:checked').length;
	  if (n <= 0) {
		  return false;
	  }
	  url += getIdsUrlParamsString();
	  jQuery.ajax({
		  url: url,
		  type: "PUT",
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
	  return false;
  });

  jQuery('input[name="checkoptionsFolder"]').change(function() {
    var checkboxes = jQuery(this).closest('.Folder:not(.row)').find('input[name="checkoptions"]');
    if (jQuery(this).is(':checked')) {
      jQuery(checkboxes).attr('checked','checked');
    } else {
      jQuery(checkboxes).removeAttr('checked');
    }
    updateSelectionBts();
  });
  
  function updateSelectionBts() {
	  var checkboxes = jQuery('input[name="checkoptions"]:checked');
	  if (jQuery(checkboxes).size() > 0) {
		  jQuery('.selectionActionsBt').closest('ul.dropdown-menu').siblings('a.dropdown-toggle').removeClass('arrowOpacity');
		  jQuery('.selectionActionsBt').closest('ul.dropdown-menu').siblings('a.dropdown-toggle').attr('data-toggle', 'dropdown');
	  } else {
		  jQuery('.selectionActionsBt').closest('ul.dropdown-menu').siblings('a.dropdown-toggle').addClass('arrowOpacity');
		  jQuery('.selectionActionsBt').closest('ul.dropdown-menu').siblings('a.dropdown-toggle').attr('data-toggle', '');
	  }
  }
  
  jQuery('input[name="checkoptions"]').change(function() {
	  updateSelectionBts();
  });

  function closeDropzone() {
    jQuery('.dropzoneContainer').html("");
  }
	
  jQuery('#form-folder').ajaxForm({ 
    success: function() { 
        location.reload();
    } 
  }); 
	
});

function renameFolder(url, id) {
    jQuery("#div-addfolder").dialog2("options",
    		{ title: "Renommer le répertoire '" + jQuery("#spanFolderTitle" + id).text() + "'" }
    );
    jQuery("#folderName").val(jQuery("#spanFolderTitle" + id).text());
    jQuery("#folderDescription").val(jQuery("#divFolderDescription" + id).html());
    jQuery('#folderDescription').ckeditor(ckeditorconfigUser);
    jQuery("#form-folder").attr('action', url);
    jQuery("#div-addfolder").dialog2('open');
    return false;
  }
  
function moveFolder(path, parentId, srcId, destId) {
    jQuery('#waitingPopup').dialog2('open');
	jQuery.ajax({
		async : false,
		type: 'POST',
		url: path + "/@pageUtils/moveFolder",
		data : {
			"source" : srcId, 
			"destinationContainer" : parentId,
			"after" : destId,
			"redirect": "true"
		},
		success : function (r) {
		   window.location.replace(path+r);
		},
		error: function(r) {
		   window.location.replace(path+r);
		}
	});
	
}

function openRenameTitleElement(title, description, url){
	jQuery("#div-renameTitleElement").dialog2('open');
	jQuery("#renameTitleElement").val(title);
	jQuery("#descriptionElement").val(description);
	jQuery("#form-renameTitleElement").attr("action", url);
    return false;
}

function bulkMove(){
    //if (confirm("Etes-vous sûr de vouloir déplacer les fichiers sélectionnés ?")) {
      jQuery('#waitingPopup').dialog2('open');

      var url = jQuery('.classeur').attr("id") + "/@pageUtils/bulkMove?";
      var n = jQuery('input[name="checkoptions"]:checked').length;
      if (n <= 0) {
        return false;
      }
      url += getIdsUrlParamsString() + "&destinationContainer=" + jQuery("#moveElementsSelectedFolder:checked").val();
      jQuery.ajax({
        url: url,
        type: "POST",
        complete: function(jqXHR, textStatus) {
        	jQuery('#waitingPopup').dialog2('close');	
          jQuery('input[name="checkoptions"]').attr('checked', false);
        },
        success: function (data, textStatus, jqXHR) {
        	jQuery('#waitingPopup').dialog2('close');
          document.location.href = jQuery('.classeur').attr("id") + data;
        },
        error: function(jqXHR, textStatus, errorThrown) {
          alert(errorThrown + ": " + "," + jqXHR.responseText);
        }
      });
      jQuery('#waitingPopup').dialog2('close');
    //}
    return false;
}

function getIdsUrlParamsString() {
	  var url = '';
	  var checkboxes = jQuery('input[name="checkoptions"]:checked');
	  var n = jQuery(checkboxes).size();
    jQuery(checkboxes).each(function(i) {
        url += "id=" + this.value;
        if (i < n-1) {
          url += "&";
        }
    });
    return url;
}
