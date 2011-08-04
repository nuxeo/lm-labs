

function deleteLabsSite(url, path){
	jQuery.ajax({
		type: "DELETE",
		url: url,
		data: '',
		success: function(msg){
			document.location.href=path;
		},
		error: function(msg){
			alert( msg.responseText );
		}
	});
}

function modifyLabsSite(url){
	jQuery.ajax({
		type: "PUT",
		url: url,
		data: '',
		success: function(msg){
			$("#editSite")[0].innerHTML = msg;	
			displayEdit();
		},
		error: function(msg){
			alert( msg.responseText );
		}
	});
}

function manageDisplayEdit(){
	if ($("#editSite")[0].style.display == "block"){
		hideEdit();
	}
	else{
		initFields();
		displayEdit();
	}
}	     

function displayEdit(){
	$("#editSite")[0].style.display= "block";
	$("#editSite")[0].style.overflow= "hidden";
}

function hideEdit(){
	$("#editSite")[0].style.display= "none";
}

function initFields(){
	jQuery("#form-labssite").clearForm();
}