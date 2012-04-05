$(document).ready(function() {
  prettyPrint();
  
  jQuery("#div-modifyCSSLine").dialog2({
    autoOpen : false,
	closeOnOverlayClick : true,
	removeOnClose : false,
	showCloseHandle : true,
  });
});

function openModifiyCSSLine(url, cssName){
	jQuery("#div-modifyCSSLine").dialog2('open');
	jQuery('#form-modifyCSSLine').attr('action', url + '/@modifyCSS');
	jQuery("#cssName").val(cssName);
}

var isOpenAddLine = {};
function openAddLine(section_index){
	jQuery("#divAddRow_" + section_index).slideDown("slow");
	jQuery("#actionAddLineOnSection_" + section_index).hide();
	isOpenAddLine[section_index] = true;
}

function closeAddLine(section_index){
	jQuery("#divAddRow_" + section_index).slideUp("slow");
	jQuery("#actionAddLineOnSection_" + section_index).show();
	isOpenAddLine[section_index] = false;
}

function actionAddLine(section_index){
	if(!(isOpenAddLine[section_index])){
		isOpenAddLine[section_index] = false;
	}
	if(isOpenAddLine[section_index] == false){
		openAddLine(section_index);
	}
	else{
		closeAddLine(section_index);
	}
}