<style>
label.error { float: none; color: red; font-size:12px; padding-left: .5em;  }
.actionExternalURL {
    text-align: right;
}
.actionExternalURL>IMG {
    cursor:pointer;
}
#div_externalURL ul {
    margin: 0 0 0 0.5em;
}
#div_externalURL ul li {
    display: block;
    text-decoration: none;
    height: 1.5em;
}
.actionExternalURL {
    float: right;
}
.actionExternalURL img {
    float: left;
}
.actionExternalURL.actionAdd {
    width: 100%;
}
</style>

<div  class="bloc" >
    <div class="header">${Context.getMessage('label.externalURL.title')}</div>
    <ul class="unstyled">
     <#list Common.siteDoc(Document).site.externalURLs as e >
       <li>
    <a href="${e.getURL()}" style="word-wrap: break-word" target="_blank" title="${e.getURL()}">${e.name}</a>
    <#if Session.hasPermission(This.document.ref, 'Everything')>
      <div class="actionExternalURL editblock">
        <img onClick="javascript:modifyExternalURL('${e.name?js_string}', '${e.getURL()}', '0', '${e.document.id}');" title="Modifier" alt="Modifier" src="${skinPath}/images/edit.gif" />
        <img onClick="javascript:deleteExternalURL('${Context.modulePath}/${Common.siteDoc(Document).site.URL}/@externalURL/${e.document.id}', '${This.path}');" title="Supprimer" alt="Supprimer" src="${skinPath}/images/x.gif" />
      </div>
    </#if>
    </li>
  </#list>
  <#if Session.hasPermission(This.document.ref, 'Everything')>
    <li>
    <div class="actionExternalURL actionAdd editblock">
    <img class="actionExternalURL" title="Ajouter" alt="Ajouter" src="${skinPath}/images/add.png" />
    </div>
    </li>
  </#if>
    </ul>
</div>

<div id="div_persistExternalURL" style="display: none;" >
  <h1>${Context.getMessage('label.externalURL.edit.title')}</h1>
  <form method="post" id="form-externalURL" enctype="multipart/form-data"
  	action="${Context.modulePath}/${Common.siteDoc(Document).site.URL}/@externalURL">
  	<fieldset>
  	  <div class="clearfix">
        <label for="exturl:name">${Context.getMessage('label.externalURL.edit.name')}</label><br />
        <div class="input">
          <input type="text" class="required" id="exturl:name" name="exturl:name" size="60" value="" />
        </div>
  	  </div>
  	  <div class="clearfix">
        <label for="exturl:url">${Context.getMessage('label.externalURL.edit.url')}</label><br />
        <div class="input">
          <input type="text" class="required" id="exturl:url" name="exturl:url" size="60" value="" />
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

function formatUrl(url) {
  url = jQuery.trim(url);
  if (url.toLowerCase().indexOf("http://") == 0) {
    return(url);
  } else if (url.toLowerCase().indexOf("ftp") == 0) {
	    if (url.toLowerCase().indexOf("ftp://") == 0) {
	      return(url);
	    } else {
	      return("http://" + url);
	    }
  } else if(url.toLowerCase().indexOf("https://") == 0) {
  		return url;
  }	else if (url.length > 0) {
    	return("http://" + url);
  } else {
    return "";
  }
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
		width: 800,
		modal: true,
		showCloseHandle : false,
		autoOpen: false
	});
	jQuery(".actionExternalURL.actionAdd > img").click(function() {
		jQuery("#div_persistExternalURL").dialog2('open');
		initFieldsExternalURL();
		return false;
	});
});
</script>