jQuery(document).ready(function() {
	var nbPictUploaded=0;
	jQuery('.DropZone').filedrop({
		url: /*${This.path} +*/ "/" + jQuery(this).attr('id') + "/addFile",
		paramname: 'userfile',
		maxfiles: 50,
		maxfilesize: 5,    // max file size in MBs
		error: function(err, file) {
			switch(err) {
		    	case 'BrowserNotSupported':
					alert('browser does not support html5 drag and drop');
					break;
				case 'TooManyFiles':
//					var tooManyFilesMsg = jQuery('#tooManyFilesMsg').val();
//					tooManyFilesMsg = tooManyFilesMsg.replace('{0}',this.maxfiles);
//					alert(tooManyFilesMsg);
					break;
		    	case 'FileTooLarge':
//					var tooLargeFileMsg = jQuery('#tooLargeFileMsg').val();
//					tooLargeFileMsg = tooLargeFileMsg.replace('{0}',this.maxfilesize);
//					alert(tooLargeFileMsg);
					break;
		    	default:
					break;
			}
		},
		dragOver: function() {
			alert('ICI');
//			jQuery(this).closest('.Folder').addClass('dropzonehighlighted');
			// user dragging files over #dropzone
		},
		dragLeave: function() {
//			jQuery(this).closest('.Folder').removeClass('dropzonehighlighted');
			// user dragging files out of #dropzone
		},
		docOver: function() {
			alert('ICI1');
			// user dragging files anywhere inside the browser document window
		},
		docLeave: function() {
			// user dragging files out of the browser document window
		},
		drop: function() {
			alert('drop');
			// user drops file
	    	jQuery('#waitingPopup').dialog2('open');
		},
		uploadStarted: function(i, file, len){
			alert('upload started');
			// a file began uploading
			// i = index => 0, 1, 2, 3, 4 etc
			// file is the actual file of the index
			// len = total files user dropped
//			if(nbPictUploaded == 0) {
//				jQuery('#waitingPopup').dialog({ modal: true });
//			}
			nbPictUploaded = nbPictUploaded+1;
//			var nbPictures = parseInt(jQuery('#nbPictures').attr("value"))+1;
//			jQuery('#nbPictures').attr("value",nbPictures);
			return false;
		},
		uploadFinished: function(i, file, response, time) {
			// response is the data you got back from server in JSON format.
			nbPictUploaded = nbPictUploaded - 1;
			if(0 == nbPictUploaded) {
//				jQuery('#waitingPopup').dialog( "close" );
		
//				var nbPictures = parseInt(jQuery('#nbPictures').attr("value"));
//				jQuery('#nbPicturesLabel').text(nbPictures);
//				var rootPath = jQuery('#rootPath').val();
//				jQuery('#picturesList').load(rootPath + "/@views/picturesList", function() {
//					disposeJSHandler();
//				});
				window.location.reload();
			}
		},
		progressUpdated: function(i, file, progress) {
			// this function is used for large files and updates intermittently
			// progress is the integer value of file being uploaded percentage to completion
		},
		speedUpdated: function(i, file, speed) {
			// speed in kb/s
		},
	    afterAll: function() {
	        // runs after all files have been uploaded or otherwise dealt with
	    	jQuery('#waitingPopup').dialog2('close');
	    },
	    rename: function(name) {
	        // name in string format
	        // must return alternate name as string
	    },
	    beforeEach: function(file) {
			alert('beforeEach');
	        // file is a file object
	        // return false to cancel upload
	    }
	});
});
