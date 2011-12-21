<div id="div_externalURL" class="bloc" >
    <div class="header">${Context.getMessage('label.externalURL.title')}</div>
    <ul id="ulExtURL" class="unstyled">
	     <#list Common.siteDoc(Document).site.externalURLs as e >
	       <li id="${e.document.id}">
			    <a href="${e.getURL()}" style="word-wrap: break-word;" target="_blank" title="${e.getURL()}">${e.name}</a>
			    <#if Session.hasPermission(This.document.ref, 'Everything') || Session.hasPermission(This.document.ref, 'ReadWrite')>
			      <div class="actionExternalURL editblock">
			        <img onClick="javascript:moveUpExternalURL('${Context.modulePath}/${Common.siteDoc(Document).site.URL}/@externalURL/${e.document.id}', '${This.path}', '${e.document.id}');" title="Monter" alt="Monter" src="${skinPath}/images/arrow-up.png" />
			        <img onClick="javascript:moveDownExternalURL('${Context.modulePath}/${Common.siteDoc(Document).site.URL}/@externalURL/${e.document.id}', '${This.path}', '${e.document.id}');" title="Descendre" alt="Descendre" src="${skinPath}/images/arrow-down.png" />
			        <img onClick="javascript:modifyExternalURL('${e.name?js_string}', '${e.getURL()}', '0', '${e.document.id}');" title="Modifier" alt="Modifier" src="${skinPath}/images/edit.gif" />
			        <img onClick="javascript:deleteExternalURL('${Context.modulePath}/${Common.siteDoc(Document).site.URL}/@externalURL/${e.document.id}', '${This.path}');" title="Supprimer" alt="Supprimer" src="${skinPath}/images/x.gif" />
			      </div>
			    </#if>
		    </li>
	  	</#list>
    </ul>
	  <#if Session.hasPermission(This.document.ref, 'Everything') || Session.hasPermission(This.document.ref, 'ReadWrite')>
	    <div class="actionExternalURL actionAdd editblock">
	    	<img class="actionExternalURL" title="Ajouter" alt="Ajouter" src="${skinPath}/images/add.png" />
	    </div>
	  	<br />
	  </#if>
</div>

<div id="div_persistExternalURL" style="display: none;" >
  <h1>${Context.getMessage('label.externalURL.edit.title')}</h1>
  <form method="post" id="form-externalURL" enctype="multipart/form-data"
  	action="${Context.modulePath}/${Common.siteDoc(Document).site.URL}/@externalURL">
  	<fieldset>
  	  <div class="clearfix">
        <label for="exturl:name">${Context.getMessage('label.externalURL.edit.name')}</label>
        <div class="input">
          <input type="text" class="required" id="exturl:name" name="exturl:name" />
        </div>
  	  </div>
  	  <div class="clearfix">
        <label for="exturl:url">${Context.getMessage('label.externalURL.edit.url')}</label>
        <div class="input">
          <input type="text" class="xlarge required" id="exturl:url" name="exturl:url" />
        </div>
  	  </div>
  	</fieldset>
    <div class="actions">
      <button class="btn primary required-fields" form-id="form-externalURL">Envoyer</button>
    </div>
  </form>
</div>

<script type="text/javascript">

function deleteExternalURL(url, path) {
	if (confirm("${Context.getMessage('label.externalURL.delete.confirm')}")) {
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
		jQuery.ajax({
			type: "GET",
			url: url + "/@moveDownExternalURL/" + jQuery(after).attr("id"),
			data: '',		
			success: function(msg){
				alert('Sauvegardé');
			},
			error: function(msg){
				//alert( msg.responseText );
				alert('Non sauvegardé!');
				document.location.href=path;
			}
		});
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
		
		jQuery.ajax({
			type: "GET",
			url: url + "/@moveUpExternalURL/" + jQuery(before).attr("id"),
			data: '',
			success: function(msg){
				alert('Sauvegardé');
			},
			error: function(msg){
				//alert( msg.responseText );
				alert('Non sauvegardé!');
				document.location.href=path;
			}
		});
	}
}

function initFieldsExternalURL() {
  jQuery("#form-externalURL").clearForm();
  jQuery('input[name="exturl:url"]').val('http://');
  jQuery("#form-externalURL div.clearfix.error").removeClass("error");
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
		showCloseHandle : true,
	});
	jQuery(".actionExternalURL.actionAdd > img").click(function() {
		jQuery("#div_persistExternalURL").dialog2('open');
		initFieldsExternalURL();
		return false;
	});
});
</script>