function initFileDrop(dropzoneId, upload_url, callback_function, input_id, tooManyFilesMsg, fileTooLargeMsg) {
	jQuery('#'+dropzoneId).filedrop({
	    url: upload_url,
	    data: {
	        no_redirect: '1',
	        id: function() {
	        	return jQuery('#'+input_id).val();
	        }
	    },
	    error: function(err, file, i, status) {
	        switch(err) {
	            case 'BrowserNotSupported':
	                alert('browser does not support html5 drag and drop');
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
	        callback_function();
	    },
	    maxfiles: 25,
	    maxfilesize: 290, // max file size in MBs. Maybe still not enough ...
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
	        // user drops file
	    	var percentVal = '0%';
	    	jQuery('#waitingPopup').find('div.bar').width(percentVal);
	    	jQuery('#waitingPopup').find('h3').html(percentVal);
	    	jQuery('#waitingPopup').dialog2('open');
	    },
	    uploadStarted: function(i, file, len){
	        // a file began uploading
	        // i = index => 0, 1, 2, 3, 4 etc
	        // file is the actual file of the index
	        // len = total files user dropped
	    	jQuery('#waitingPopup').find('h3').html(i + '/' + len + ' fichiers transférés');
	        return false;
	    },
	    uploadFinished: function(i, file, response, time) {
            jQuery('#waitingPopup').find('h3').html('Téléchargements en cours de finalisation ...');
	    	jQuery('#'+dropzoneId).removeClass('dropzonehighlighted');
	    	jQuery('#waitingPopup').dialog2('close');
	    	callback_function();
	    },
	    progressUpdated: function(i, file, progress) {
	        // this function is used for large files and updates intermittently
	        // progress is the integer value of file being uploaded percentage to completion
	    },
	    globalProgressUpdated: function(progress) {
	        // progress for all the files uploaded on the current instance (percentage)
	        // ex: $('#progress div').width(progress+"%");
            var percentVal = progress + '%';
            jQuery('#waitingPopup').find('div.bar').width(percentVal);
            jQuery('#waitingPopup').find('h3').html(percentVal);
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
	    	callback_function();
	    }
	});
}