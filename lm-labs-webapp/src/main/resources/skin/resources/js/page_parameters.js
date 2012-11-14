function openParametersPage(){
    jQuery("#divEditParametersPage").dialog2('open');
    divEditParametersPageForm = jQuery("#divEditParametersPage")[0].innerHTML;
}

function closeParametersPage(){
    jQuery("#divEditParametersPage").dialog2('close');
    jQuery("#divEditParametersPage")[0].innerHTML = divEditParametersPageForm;
}

function openAddContentView(url){
    jQuery("#divAddContentView").dialog2('open');
    jQuery("#divAddContentView").dialog2('load', url);
}

function changeAssistant(modeCreation){
	if (modeCreation == "assistant"){
		$("#divAssistantTypePage").hide();
		$("#divAssistantContent").show();
	}
	else{
		$("#divAssistantTypePage").show();
		$("#divAssistantContent").hide();
	}
}

function loadPagesTemplate(url, skinPath, element){
	$("#divAssistantPages").html('<img src="'+skinPath+'/images/loading.gif" />');
	cleanLiActive($(element).parent().parent());
	$("#divAssistantPagesPreview").html("");
	$("#idPageTemplate").val("");
	$(element).parent().addClass("active");
	jQuery.ajax({
		type: "GET",
		url: url,
		data: '',
		success: function(html){
			$("#divAssistantPages").html(html);
		},
		error: function(msg){
			alert( msg.responseText );
			$("#divAssistantPages").html('<img src="'+skinPath+'/images/x.gif" />');
		}
	});
}

function loadPreviewTemplate(url, skinPath, element){
	cleanLiActive($(element).parent().parent());
	$(element).parent().addClass("active");
	$("#idPageTemplate").val($(element).attr("refPage"));
	if (url == "noPreview"){
		$("#divAssistantPagesPreview").html('<h4>Pas de prévisualisation <small>pour ce modèle</small></h4>');
	}
	else{
		$("#divAssistantPagesPreview").html('<img src="'+url+'" />');
	}
}

function cleanLiActive(parent){
	$(parent).children("li").each(function(index, element) {
	    if ($(element).hasClass("active")){
	    	$(element).removeClass("active");
	    }
	});
}
