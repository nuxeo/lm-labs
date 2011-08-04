

function deleteLabsSite(url, path){
	jQuery.ajax({
		type: "DELETE",
		url: url,
		data: '',
		success: function(msg){
			document.location.href=path + '?homepage=display';
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

function sendForm(path, msgError){
	$("#form-labssite").validate();
    var options = {  
		beforeSubmit: function(){
			return $("#form-labssite").valid();
		},
        success: function(responseText, statusText) {
        	if (statusText == "notmodified"){
        		$("#labsSiteURL").val($("#oldURL").val());
				alert(msgError);
				//alert("${Context.getMessage('label.labssites.edit.error')}");
        	}else{
        		document.location.href=path + '?homepage=display';
        	}
        },
        error: function(){
          alert("ERROR");
        }
    };
    $('#form-labssite').ajaxForm(options);
}

jQuery(document).ready(function(){
	jQuery("#divEditSite").dialog({
		dialogClass: 'create-labs-site',
		open: function() {initFields();},
		buttons: {"Annuler": function() { jQuery(this).dialog("close"); }},
		width: 800,
		modal: true,
		autoOpen: false
	});


	jQuery("#bt_create_labssite").click(function() {
		jQuery("#divEditSite").dialog('open');
		return false;
	});

});