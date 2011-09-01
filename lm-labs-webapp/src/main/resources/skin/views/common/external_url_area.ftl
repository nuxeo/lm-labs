<style>
	label.error { float: none; color: red; font-size:12px; padding-left: .5em;  }
	.actionExternalURL{
		text-align: right;
	}
	.actionExternalURL>IMG{
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

<div id="div_externalURL" class="sidebarzone" >
		<div class="title">${Context.getMessage('label.externalURL.title')}</div>
		<ul>
   	<#list This.externalURLs as e >
   		<li>
		<a href="${e.getURL()}" style="word-wrap: break-word" target="_blank" title="${e.getURL()}">${e.name}</a>
		<#if Session.hasPermission(This.document.ref, 'Everything')>
			<div class="actionExternalURL">
				<img onClick="javascript:modifyExternalURL('${This.escapeJS(e.name)}', '${e.getURL()}', '${e.order}', '${e.document.id}');" title="Modifier" alt="Modifier" src="${skinPath}/images/edit.gif" />
				<img onClick="javascript:deleteExternalURL('${This.path}/deleteExternalURL/${e.document.id}', '${This.path}');" title="Supprimer" alt="Supprimer" src="${skinPath}/images/x.gif" />
			</div>
		</#if>
		</li>
	</#list>
	<#if Session.hasPermission(This.document.ref, 'Everything')>
		<li>
		<div class="actionExternalURL actionAdd"><img class="actionExternalURL" title="Ajouter" id="addExternalURL" alt="Ajouter" src="${skinPath}/images/add.png" /></div>
		</li>
	</#if>
		</ul>
</div>

<div id="div_persistExternalURL" style="display: none;" title="${Context.getMessage('label.externalURL.edit.title')}">
	<form method="post" id="form-externalURL" action="${This.path}/persistExternalURL">
		<ul class="fieldEditExternalURL">
			<li>
				<label for="extUrlName">${Context.getMessage('label.externalURL.edit.name')}</label><br />
				<input type="text" class="required" name="extUrlName" id="extUrlName" size="60" value="" />
			</li>
			<li>
				<label for="extURLURL">${Context.getMessage('label.externalURL.edit.url')}</label><br />
    			<input type="text" class="required" name="extURLURL" size="60" id="extURLURL" value="" />
			</li>
			<li>
				<label for="extURLOrder">${Context.getMessage('label.externalURL.edit.order')}</label><br />		
    			<input type="text" name="extURLOrder" id="extURLOrder"size="5" class="extURLOrder" value="" />
			</li>
		</ul>
	</form>
</div>
	
<script type="text/javascript">
	$(document).ready(function() {
	    $.validator.messages.required = "${Context.getMessage('label.externalURL.edit.required')}";
	});

function deleteExternalURL(url, path){
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

	
function initFieldsExternalURL(){
	jQuery("#form-externalURL").clearForm();
}

function modifyExternalURL(name, url, order, id){
	initFieldsExternalURL();
	jQuery("#div_persistExternalURL").dialog('open');
	$('#extUrlName').val(name);
	$('#extURLURL').val(url);
	$('#extURLOrder').val(order);
	$('#form-externalURL').attr("action", $('#form-externalURL').attr("action") + "/" + id);
}


jQuery(document).ready(function(){
	jQuery("#div_persistExternalURL").dialog({
		dialogClass: 'edit-external-url',
		open: function() {initFieldsExternalURL();},
		buttons: {
			"Annuler": function() { jQuery(this).dialog("close"); },
			"valider": function(evt) {
			
				var options2 = {  
					beforeSubmit: function(){
						return $("#form-externalURL").valid();
					},
			        success: function(responseText, statusText) {
			        	jQuery(this).dialog("close");
			        	document.location.href='${This.path}';
			        },
			        error: function(){
			          alert("ERROR");
			          jQuery(this).dialog("close");
			        }
			    };
			    $('#form-externalURL').ajaxSubmit(options2);
				return true;
			}
		},
		width: 800,
		modal: true,
		autoOpen: false
	});

	jQuery("#addExternalURL").click(function() {
		jQuery("#div_persistExternalURL").dialog('open');
		return false;
	});

});	

</script>