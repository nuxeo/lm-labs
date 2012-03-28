jQuery(document).ready(function() {
	initModal();
});


function initModal(){
	jQuery("#divEditLine").dialog2({
		width : '530px',
		height : '400px',
		overflowy : 'auto',
		overflowx : 'hidden',
		autoOpen : false,
		closeOnOverlayClick : true,
		removeOnClose : false,
		showCloseHandle : true,
	});
}

function addLine() {
	openEditLine();
	jQuery("#divEditLine").dialog2("options", {title: title_add_line});
	$('#form-editLine').clearForm();
	$("#divBtnDeleteLine").attr("style", "display:none;");
	var action = $('#form-editLine').attr('action');
	var addLineSuffix = '/addline/@put';
	if (action.indexOf('/@put') == -1){
		$('#form-editLine').attr('action', action + addLineSuffix);
	}
}

function openEditLine() {
	jQuery("#divEditLine").dialog2('open');
	initFieldsLine();
}

function closeEditLine() {
	jQuery("#divEditLine").dialog2('close');
}

function initFieldsLine() {
	initEditLinesDates();
}

function initEditLinesDates(){
	$(function() {
		var dates = $( ".date-pick" )
			.datepicker({
				dateFormat: "dd/mm/yy",
				changeMonth: true,
				numberOfMonths: 1,
				firstDay: 1,
			});		
		});
} 

function saveLine(path) {
	jQuery('#waitingPopup').dialog2('open');
	var action = $('#form-editLine').attr('action');
	if (action.indexOf('/@put') == -1){
		action = action + '/@put';
	}
	jQuery.ajax({
		type: "POST",
		url: action,
		data: $("#form-editLine").serialize(),
		success: function(msg){
			document.location.href=path + msg;
		},
		error: function(msg){
			alert( msg.responseText );
			jQuery('#waitingPopup').dialog2('close');
		}
	});
}

function deleteLine(path){
	jQuery('#waitingPopup').dialog2('open');
	jQuery.ajax({
		type: "DELETE",
		url: $('#form-editLine').attr("action"),
		data: '',
		success: function(msg){
			document.location.href=path + msg;
		},
		error: function(msg){
			alert( msg.responseText );
			jQuery('#waitingPopup').dialog2('close');
		}
	});
}

function modifyLine(url){
	jQuery('#waitingPopup').dialog2('open');
	jQuery.ajax({
		type: "GET",
		url: url,
		data: '',
		success: function(msg){
			$("#divEditLine")[0].innerHTML = msg;
			jQuery('#waitingPopup').dialog2('close');
			openEditLine();
			jQuery("#divEditLine").dialog2("options", {title: title_modify_line});
		},
		error: function(msg){
			alert( msg.responseText );
			jQuery('#waitingPopup').dialog2('close');
		}
	});
}