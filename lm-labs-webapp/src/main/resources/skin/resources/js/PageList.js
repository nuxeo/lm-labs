jQuery(document).ready(function() {
	initModal();
});

function initModal(){
	jQuery("#divEditLine").dialog2({
		width : 550,
		height : 400,
		showCloseHandle : false,
		removeOnClose : true,
		autoOpen : false
	});
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

function saveLine() {
	$('#form-editLine').submit();
}

function deleteLine(path){
	jQuery.ajax({
		type: "DELETE",
		url: $('#form-editLine').attr("action"),
		data: '',
		success: function(msg){
			document.location.href=path;
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
		},
		error: function(msg){
			alert( msg.responseText );
		}
	});
}