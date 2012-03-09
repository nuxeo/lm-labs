

function deleteNews(url, path){
	jQuery('#waitingPopup').dialog2('open');
	jQuery.ajax({
		type: "DELETE",
		url: url,
		data: '',
		success: function(msg){
			document.location.href=path;
		},
		error: function(msg){
			alert( msg.responseText );
			jQuery('#waitingPopup').dialog2('close');
		}
	});
}

function modifyNews(url){
	jQuery('#waitingPopup').dialog2('open');
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
			jQuery('#waitingPopup').dialog2('close');
		},
		error: function(msg){
			alert( msg.responseText );
			jQuery('#waitingPopup').dialog2('close');
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
function openNews(id, skinBase){
	jQuery("#contentNews" + id).slideDown("slow");
	jQuery("#summaryNews" + id).hide();
	isOpen = true;
}

function closeNews(id, skinBase){
	jQuery("#contentNews" + id).hide();
	jQuery("#summaryNews" + id).show();
	isOpen = false;
}

function actionNews(id, skinBase){
	if(isOpen == false){
		openNews(id, skinBase);
	}
	else{
		closeNews(id, skinBase);
	}
}
