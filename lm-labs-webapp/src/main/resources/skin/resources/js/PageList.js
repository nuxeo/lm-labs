jQuery(document).ready(function() {
	initModal();
});


function initModal(){
	jQuery("#divEditLine").dialog2({
		width : '530px',
		height : '400px',
		overflowy : 'auto',
		overflowx : 'hidden',
		showCloseHandle : false,
		removeOnClose : true,
		autoOpen : false
	});
}

function addLine() {
	openEditLine();
	jQuery("#divEditLine").dialog2("options", {title: title_add_line});
	$('#form-editLine').clearForm();
	$("#divBtnDeleteLine").attr("style", "display:none;");
	$('#form-editLine').attr('action', $('#form-editLine').attr('action') + '/line');
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
	jQuery.ajax({
		type: "POST",
		url: $('#form-editLine').attr("action"),
		data: $("#form-editLine").serialize(),
		success: function(msg){
			document.location.href=path + msg;
		},
		error: function(msg){
			alert( msg.responseText );
		}
	});
}

function deleteLine(path){
	jQuery.ajax({
		type: "DELETE",
		url: $('#form-editLine').attr("action"),
		data: '',
		success: function(msg){
			document.location.href=path + msg;
		},
		error: function(msg){
			alert( msg.responseText );
		}
	});
}

function modifyLine(url){
	jQuery.ajax({
		type: "GET",
		url: url,
		data: '',
		success: function(msg){
			$("#divEditLine")[0].innerHTML = msg;
			openEditLine();
			jQuery("#divEditLine").dialog2("options", {title: title_modify_line});
		},
		error: function(msg){
			alert( msg.responseText );
		}
	});
}