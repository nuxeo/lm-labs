

function deleteNews(url, path){
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

function modifyNews(url){
	jQuery.ajax({
		type: "GET",
		url: url,
		data: '',
		success: function(msg){
			$("#editNews")[0].innerHTML = msg;	
			initEditDateNews();
			removeCheckeditor();
			initCheckeditor();
			displayEdit();
		},
		error: function(msg){
			alert( msg.responseText );
		}
	});
}

function manageDisplayEdit(){
	if ($("#editNews")[0].style.display == "block"){
		hideEdit();
	}
	else{
		initFields();
		initEditDateNews();
		displayEdit();
	}
}	    

function initEditDateNews(){
	$(function() {
		var dates = $( "#newsStartPublication, #newsEndPublication" )
			.datepicker({
				dateFormat: "dd/mm/yy",
				changeMonth: true,
				numberOfMonths: 2,
				firstDay: 1,
				onSelect: function( selectedDate ) {
					var option = this.id == "newsStartPublication" ? "minDate" : "maxDate",
						instance = $( this ).data( "datepicker" ),
						date = $.datepicker.parseDate(
							instance.settings.dateFormat ||
							$.datepicker._defaults.dateFormat,
							selectedDate, instance.settings );
					dates.not( this ).datepicker( "option", option, date );
				},
			});		
		});
} 

function displayEdit(){
	$("#editNews")[0].style.display= "block";
	$("#editNews")[0].style.overflow= "hidden";
}

function hideEdit(){
	$("#editNews")[0].style.display= "none";
}

function initFields(){
	$("#newsTitle").val("");
	$("#newsContent").val("");
	$("#newsStartPublication").val("");
	$("#newsEndPublication").val("");
	$("#newsAccroche").val("");
	$("#newsId").val("");
	CKEDITOR.instances.newsContent.setData("");
}

function initCheckeditor(){
	CKEDITOR.replace( 'newsContent' );
}

function removeCheckeditor(){
	CKEDITOR.remove(CKEDITOR.instances.newsContent);
}

var isOpen = false;
function openPropsNews(){
	jQuery("#editprops").slideDown("slow", function(){
		doEllipsisText();
		doEllipsisText();
	});
	jQuery("#btnModifyPropsNews").html("Fermer les propriétés");
	isOpen = true;
}

function closePropsNews(){
	jQuery("#editprops").slideUp("slow");
	jQuery("#btnModifyPropsNews").html("Modifier les propriétés");
	isOpen = false;
}

function actionPropsNews(){
	if(isOpen == false){
		openPropsNews();
	}
	else{
		closePropsNews();
	}
}

var cropCoords;
var jcrop_api;
var widthSummary;
var heightSummary;

$(document).ready(function() {
	jQuery("#divCropPicture").dialog2({
		width : '900px',
		height : '600px',
		overflowy : 'auto',
		overflowx : 'auto',
		autoOpen : false,
		closeOnOverlayClick : true,
		removeOnClose : false,
		showCloseHandle : true,
		buttons: {
			"Annuler": function() { jQuery(this).dialog2("close"); },
		    "Réinitialisation": function() { resetCropCoords(); },
		    "Valider": function() { saveCropCoords(); }
		},
	});
	
	if (jQuery("#cropSummaryPicture").val() != ""){
		cropCoords = eval('(' + jQuery("#cropSummaryPicture").val() + ')');
	}
});

function openCropPicture(){
	jQuery("#divCropPicture").dialog2("open");
	widthSummary = jQuery("#summaryPicture").width();
	heightSummary = jQuery("#summaryPicture").height();
	$('#summaryPicture').Jcrop({
		onSelect: setCoords,
		onChange: setCoords,
		aspectRatio: 4/3,
		setSelect: extractTabCropCoords(),
		minSize: [ 120, 90 ]
	},function(){
		  jcrop_api = this;
		  setCoords(cropCoords);
	});
}

function extractTabCropCoords(){
	if (jQuery("#cropSummaryPicture").val() != ""){
		return [
				cropCoords.x,
				cropCoords.y,
				cropCoords.x2,
				cropCoords.y2
			];
	}
	else{
		var width = jQuery("#summaryPicture").width();
		var height = jQuery("#summaryPicture").height();
		return [
				(width / 2) - 60,
				(height / 2) - 45,
				(width / 2) + 60,
				(height / 2) + 45
			];
	}
}

function setCoords(c){
	cropCoords = c;
	var rx = 120 / cropCoords.w;
	var ry = 90 / cropCoords.h;

	$('#summaryPicturePreview').css({
		width: Math.round(rx * widthSummary) + 'px',
		height: Math.round(ry * heightSummary) + 'px',
		marginLeft: '-' + Math.round(rx * cropCoords.x) + 'px',
		marginTop: '-' + Math.round(ry * cropCoords.y) + 'px'
	});
};

function saveCropCoords(){
	jQuery("#cropSummaryPicture").val(JSON.stringify(cropCoords));
	jQuery("#divCropPicture").dialog2("close");
	jQuery("#form-editNews").submit();
}

function resetCropCoords(){
	if (jQuery("#cropSummaryPictureOrigin").val() != ""){
		cropCoords = eval('(' + jQuery("#cropSummaryPictureOrigin").val() + ')');
	}
	else{
		cropCoords = "";
	}
	jcrop_api.animateTo(extractTabCropCoords());
}

$(document).ready(function() {
	jQuery("#divDownloadPicture").dialog2({
		autoOpen : false,
		buttons: {
			"Annuler": function() { jQuery(this).dialog2("close"); }
		},
	});
});

function openDownloadPicture(){
	jQuery("#divDownloadPicture").dialog2("open");
	jQuery("#form-downloadPicture").clearForm();
}

function savePicture(){
	jQuery("#newsPicture").appendTo('#form-editNews');
	jQuery("#divDownloadPicture").dialog2("close");
	jQuery("#form-editNews").submit();

}
