jQuery(document).ready(function(){
	jQuery("#divManageList").dialog({
		dialogClass: 'manage-list',
		open: function() {initFields();},
		//buttons: {"Annuler": function() { jQuery(this).dialog("close"); }},
		width: 500,
		modal: true,
		autoOpen: false
	});

});

function initFields(){
	
}

function manageList(){
	jQuery("#divManageList").dialog('open');
}

function closeManageList(){
	jQuery("#divManageList").dialog('close');
}