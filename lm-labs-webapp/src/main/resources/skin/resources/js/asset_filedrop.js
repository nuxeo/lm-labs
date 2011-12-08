function initFileDrop(dropzoneId, base_url, callback_function, inputId) {
	jQuery('#'+dropzoneId).filedrop({
	    url: base_url,
	    data: {
	        no_redirect: '1',
	        id: function() {
	        	return jQuery('#'+inputId).val();
	        }
	    },
	    error: function(err, file) {
	        switch(err) {
	            case 'BrowserNotSupported':
	                alert('browser does not support html5 drag and drop')
	                break;
	            case 'TooManyFiles':
	                // user uploaded more than 'maxfiles'
	                break;
	            case 'FileTooLarge':
	                // program encountered a file whose size is greater than 'maxfilesize'
	                // FileTooLarge also has access to the file which was too large
	                // use file.name to reference the filename of the culprit file
	                break;
	            default:
	                break;
	        }
	    },
	    maxfiles: 25,
	    maxfilesize: 5,    // max file size in MBs
	    dragOver: function() {
	        // user dragging files over #dropzone
	    },
	    dragLeave: function() {
	        // user dragging files out of #dropzone
	    },
	    docOver: function() {
	        // user dragging files anywhere inside the browser document window
	    },
	    docLeave: function() {
	        // user dragging files out of the browser document window
	    },
	    drop: function() {
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
	        callback_function();
	    }
	});
}