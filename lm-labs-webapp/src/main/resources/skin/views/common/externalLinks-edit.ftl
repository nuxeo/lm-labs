
<#assign externalLinkInputDivId = "div_persistExternalURL" />
<div id="${externalLinkInputDivId}" style="display: none;" >
  <h1>${Context.getMessage('label.externalURL.edit.title')}</h1>
  <form class="form-horizontal" method="post" id="${externalLinkInputDivId}-form" enctype="multipart/form-data"
  	action="${Context.modulePath}/${mySite.URL}/@externalURL">
  	<fieldset>
  	  <div class="control-group">
        <label class="control-label" for="exturl:name">${Context.getMessage('label.externalURL.edit.name')}</label>
        <div class="controls">
          <input type="text" class="input required" name="exturl:name" />
        </div>
  	  </div>
  	  <div class="control-group">
        <label class="control-label" for="exturl:url">${Context.getMessage('label.externalURL.edit.url')}</label>
        <div class="controls">
          <input type="text" class="required input-xlarge " name="exturl:url" />
        </div>
  	  </div>
  	</fieldset>
    <div class="actions">
      <button class="btn btn-primary required-fields" form-id="${externalLinkInputDivId}-form">${Context.getMessage('command.externalURL.edit.submit')}</button>
    </div>
  </form>
</div>

<#if mySite.isContributor(Context.principal.name) >
<script type="text/javascript">
<#--
function deleteExternalURL(url, path) {
	if (confirm("${Context.getMessage('label.externalURL.delete.confirm')}")) {
	    jQuery('.external-links > ul').html('<img src="${skinPath}/images/loading.gif" />');
		jQuery.ajax({
			type: "DELETE",
			url: url,
			data: '',
			success: function(msg){
				jQuery('.external-links > ul').load('${Context.modulePath}/${mySite.URL}/@views/externalLinksList');
			},
			error: function(msg){
				alert( msg.responseText );
			}
		});
	}
}
-->

function moveDownExternalURL(url, path, id){
	var finded = false;
	var after;
	var current;
	
	jQuery(".bloc.external-links > ul >li").each(function (index, element){
		if (finded){
			after = element;
			return false;
		}
		if (jQuery(element).attr("class") == id){
			finded = true;
			current = element;
		}
	});
	
	jQuery(current).insertAfter(after);
	
	if(jQuery(after).attr("class") != undefined){
		ajaxMoveExtURL(url + "/@moveDownExternalURL/" + jQuery(after).attr("class"));
	}
}

function moveUpExternalURL(url, path, id){
	var before;
	var current;
	
	jQuery(".bloc.external-links > ul >li").each(function (index, element){
		if (jQuery(element).attr("class") == id){
			current = element;
			return false;
		}
		else{
			before = element;
		}
	});
	jQuery(before).insertAfter(current);
	
	if(jQuery(before).attr("class") != undefined){
		ajaxMoveExtURL(url + "/@moveUpExternalURL/" + jQuery(before).attr("class"));
	}
}

function ajaxMoveExtURL(url){
	jQuery('#waitingPopup').dialog2('open');
	jQuery.ajax({
		type: "GET",
		url: url,
		data: '',
		success: function(msg){
			//alert('Sauvegardé');
			jQuery('#waitingPopup').dialog2('close');
		},
		error: function(msg){
			//alert( msg.responseText );
			alert('Non sauvegardé!');
			//document.location.href=path;
			jQuery('.external-links > ul').load('${Context.modulePath}/${mySite.URL}/@views/externalLinksList');
		}
	});
}

function modifyExternalURL(divId, name, url, order, id){
  jQuery('#' + divId).dialog2('open');
  initFieldsExternalURL(jQuery('#' + divId + ' > form'));
  jQuery('#' + divId + ' > form input[name="exturl:name"]').val(name);
  jQuery('#' + divId + ' > form input[name="exturl:url"]').val(url);
  //jQuery('#extURLOrder').val(order);
  jQuery('#' + divId + ' > form').attr("action", jQuery('#' + divId + ' > form').attr("action") + "/" + id + "/@put");
}

function initFieldsExternalURL(formObj) {
  jQuery(formObj).clearForm();
  jQuery(formObj).find('input[name="exturl:url"]').val('http://');
  jQuery(formObj).find('div.control-group.error').removeClass("error");
}

jQuery(document).ready(function(){
	jQuery('#${externalLinkInputDivId}').find('form').ajaxForm(function() { 
		jQuery('#${externalLinkInputDivId}').dialog2('close');
		jQuery('.external-links > ul').load('${Context.modulePath}/${mySite.URL}/@views/externalLinksList');
	}); 
	jQuery('#${externalLinkInputDivId}').dialog2({
		open: function() { /*initFieldsExternalURL();*/ },
		buttons: {
			"Fermer": function() { jQuery(this).dialog2("close"); }
		},
		width: '500px',
		autoOpen : false,
		closeOnOverlayClick : true,
		removeOnClose : false,
		showCloseHandle : true
	});
	jQuery('a.addExternalURLBtn').click(function() {
		jQuery('#${externalLinkInputDivId}').dialog2('open');
		initFieldsExternalURL(jQuery('#${externalLinkInputDivId} > form'));
		return false;
	});
});
</script>
</#if>