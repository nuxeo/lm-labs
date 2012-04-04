<#assign mySite=Common.siteDoc(Document).site />
<div id="div_externalURL" class="bloc" >
    <div class="header">${Context.getMessage('label.externalURL.title')}</div>
    <ul id="ulExtURL" class="unstyled">
	     <#list mySite.externalURLs as e >
	       <li id="${e.document.id}" class="oneExternalURL">
			    <a href="${e.getURL()}" style="word-wrap: break-word;" target="_blank" title="${e.getURL()}">${e.name}</a>
			    <#if mySite.isContributor(Context.principal.name)>
			      <div class="actionExternalURL editblock btn-group">
			      	<a class="btn btn-primary btn-mini dropdown-toggle" data-toggle="dropdown" style="padding: 0px 4px 2px 3px;margin-top: 4px;"><span class="caret"></span></a>
    				<ul class="dropdown-menu" >
    					<li>
    						<a href="#" onClick="javascript:moveUpExternalURL('${Context.modulePath}/${mySite.URL}/@externalURL/${e.document.id}', '${This.path}', '${e.document.id}');" title="Monter" alt="Monter"><i class="icon-arrow-up"></i>Monter</a>
    						<a href="#" onClick="javascript:moveDownExternalURL('${Context.modulePath}/${mySite.URL}/@externalURL/${e.document.id}', '${This.path}', '${e.document.id}');" title="Descendre" alt="Descendre"><i class="icon-arrow-down"></i>Descendre</a>
    						<a href="#" onClick="javascript:modifyExternalURL('${e.name?js_string}', '${e.getURL()}', '0', '${e.document.id}');" title="Modifier" alt="Modifier"><i class="icon-edit"></i>Modifier</a>
    						<a href="#" onClick="javascript:deleteExternalURL('${Context.modulePath}/${mySite.URL}/@externalURL/${e.document.id}', '${This.path}');" title="Supprimer" alt="Supprimer"><i class="icon-remove"></i>Supprimer</a>
    					</li>
    				</ul>
			      </div>
			    </#if>
		    </li>
	  	</#list>
    </ul>
	  <#if mySite.isContributor(Context.principal.name)>
	    <div class="editblock">
	    	<a href="#" id="addExternalURL" title="Ajouter" alt="Ajouter"><i class="icon-plus"></i></a>
	    </div>
	  	<br />
	  </#if>
</div>

<div id="div_persistExternalURL" style="display: none;" >
  <h1>${Context.getMessage('label.externalURL.edit.title')}</h1>
  <form class="form-horizontal" method="post" id="form-externalURL" enctype="multipart/form-data"
  	action="${Context.modulePath}/${mySite.URL}/@externalURL">
  	<fieldset>
  	  <div class="control-group">
        <label class="control-label" for="exturl:name">${Context.getMessage('label.externalURL.edit.name')}</label>
        <div class="controls">
          <input type="text" class="input required" id="exturl:name" name="exturl:name" />
        </div>
  	  </div>
  	  <div class="control-group">
        <label class="control-label" for="exturl:url">${Context.getMessage('label.externalURL.edit.url')}</label>
        <div class="controls">
          <input type="text" class="required input-xlarge " id="exturl:url" name="exturl:url" />
        </div>
  	  </div>
  	</fieldset>
    <div class="actions">
      <button class="btn btn-primary required-fields" form-id="form-externalURL">Envoyer</button>
    </div>
  </form>
</div>

<script type="text/javascript">

function deleteExternalURL(url, path) {
	if (confirm("${Context.getMessage('label.externalURL.delete.confirm')}")) {
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
}

function moveDownExternalURL(url, path, id){
	var finded = false;
	var after;
	var current;
	
	jQuery("#ulExtURL>li").each(function (index, element){
		if (finded){
			after = element;
			return false;
		}
		if (jQuery(element).attr("id") == id){
			finded = true;
			current = element;
		}
	});
	
	jQuery(current).insertAfter(after);
	
	if(jQuery(after).attr("id") != undefined){
		ajaxMoveExtURL(url + "/@moveDownExternalURL/" + jQuery(after).attr("id"));
	}
}

function moveUpExternalURL(url, path, id){
	var before;
	var current;
	
	jQuery("#ulExtURL>li").each(function (index, element){
		if (jQuery(element).attr("id") == id){
			current = element;
			return false;
		}
		else{
			before = element;
		}
	});
	jQuery(before).insertAfter(current);
	
	if(jQuery(before).attr("id") != undefined){
		ajaxMoveExtURL(url + "/@moveUpExternalURL/" + jQuery(before).attr("id"));
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
			document.location.href=path;
		}
	});
}

function initFieldsExternalURL() {
  jQuery("#form-externalURL").clearForm();
  jQuery('input[name="exturl:url"]').val('http://');
  jQuery("#form-externalURL div.control-group.error").removeClass("error");
}

function modifyExternalURL(name, url, order, id){
  jQuery("#div_persistExternalURL").dialog2('open');
  initFieldsExternalURL();
  jQuery('input[name="exturl:name"]').val(name);
  jQuery('input[name="exturl:url"]').val(url);
  //jQuery('#extURLOrder').val(order);
  jQuery('#form-externalURL').attr("action", jQuery('#form-externalURL').attr("action") + "/" + id + "/@put");
}

jQuery(document).ready(function(){
	jQuery('#form-externalURL').ajaxForm(function() { 
		jQuery("#div_persistExternalURL").dialog2('close');
		window.location.reload();
	}); 
	jQuery("#div_persistExternalURL").dialog2({
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
	jQuery("#addExternalURL").click(function() {
		jQuery("#div_persistExternalURL").dialog2('open');
		initFieldsExternalURL();
		return false;
	});
});
</script>