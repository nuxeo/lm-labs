function initFileDrop(dropzoneId, upload_url, callback_function, input_id, tooManyFilesMsg, fileTooLargeMsg) {
	jQuery('#'+dropzoneId).filedrop({
	    url: upload_url,
	    data: {
	        no_redirect: '1',
	        id: function() {
	        	return jQuery('#'+input_id).val();
	        }
	    },
	    error: function(err, file) {
	        switch(err) {
	            case 'BrowserNotSupported':
	                alert('browser does not support html5 drag and drop')
	                break;
	            case 'TooManyFiles':
	                alert(tooManyFilesMsg);
	                break;
	            case 'FileTooLarge':
	                // program enco reference the filename of the culprit file
	                alert(fileTooLargeMsg);
	                break;
	            default:
	            	alert("unknown error");
	                break;
	        }
	        jQuery('#'+dropzoneId).removeClass('dropzonehighlighted');
	    },
	    maxfiles: 25,
	    maxfilesize: 5,
	    dragOver: function() {
	        jQuery('#'+dropzoneId).addClass('dropzonehighlighted');
	    },
	    dragLeave: function() {
	        jQuery('#'+dropzoneId).removeClass('dropzonehighlighted');
	    },
	    docOver: function() {
	        // user dragging files anywhere inside the browser document window
	    },
	    docLeave: function() {
	        // user dragging files out of the browser document window
	    },
	    drop: function() {
	    	jQuery('#waitingPopup').dialog2('open');
	        // user drops file
	    },
	    uploadStarted: function(i, file, len){
	        return false;
	    },
	    uploadFinished: function(i, file, response, time) {
	    },
	    progressUpdated: function(i, file, progress) {
	        // this function is used for large files and updates intermittently
	        // progress is the integer value of file being uploaded percentage to completion
	    },
	    speedUpdated: function(i, file, speed) {
	        // speed in kb/s
	    },
	    rename: function(name) {
	        // name in string format
	        // must return alternate name as string
	        return name;
	    },
	    beforeEach: function(file) {
	        // file is a file object
	        // return false to cancel upload
	    },
	    afterAll: function() {
	    	jQuery('#'+dropzoneId).removeClass('dropzonehighlighted');
	    	jQuery('#waitingPopup').dialog2('close');
	        callback_function();
	    }
	});
}