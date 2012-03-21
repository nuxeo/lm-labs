<section>
    <@displayPermTypeHeader permType='Everything' permsList=permissionsAdmin >
        <button id="displayAddPerm" onClick="javascript:displayAddPerm('${Context.getMessage('label.security.labs.permission.Everything')?js_string}', 'Everything');" title="${Context.getMessage('label.security.labs.button.add') + ' ' + Context.getMessage('label.security.labs.permission.Everything')}" class="btn btn-small" style="margin-left:20px;" ><i class="icon-plus" style="padding-right:0px;"></i></button>
    </@displayPermTypeHeader>
	<#list permissionsAdmin as perm>
       <@displayPermRow perm=perm />
	</#list>
</section>

<section>
    <@displayPermTypeHeader permType='Write' permsList=permissionsWrite >
        <button id="displayAddPerm" onClick="javascript:displayAddPerm('${Context.getMessage('label.security.labs.permission.Write')?js_string}', 'ReadWrite');" title="${Context.getMessage('label.security.labs.button.add') + ' ' + Context.getMessage('label.security.labs.permission.Write')}" class="btn btn-small" style="margin-left:20px;" ><i class="icon-plus" style="padding-right:0px;"></i></button>
    </@displayPermTypeHeader>
	<#list permissionsWrite as perm>
       <@displayPermRow perm=perm />
	</#list>
</section>

<section>
    <@displayPermTypeHeader permType='Read' permsList=permissionsRead >
        <button id="vomitPublic" onClick="javascript:vomitPublic();" title="${Context.getMessage('label.security.labs.button.vomitPublic')}" class="btn btn-small btn-warning"><i class="icon-fire"></i>${Context.getMessage('label.security.labs.button.vomitPublic')}</button>
        <button id="displayAddPerm" onClick="javascript:displayAddPerm('${Context.getMessage('label.security.labs.permission.Read')?js_string}', 'Read');" title="${Context.getMessage('label.security.labs.button.add') + ' ' + Context.getMessage('label.security.labs.permission.Read')}" class="btn btn-small"><i class="icon-plus" style="padding-right:0px;"></i></button>
    </@displayPermTypeHeader>
	<#list permissionsRead as perm>
	   <@displayPermRow perm=perm />
	</#list>
</section>

<#macro displayPermRow perm >
<#assign permName = "" />
<div class="row-fluid">
    <div class="span1 columns">
        &nbsp;
    </div>
    <div class="span8 columns">
        <#if perm.name == 'Everyone' >
            ${Context.getMessage('label.security.labs.' + perm.name)}
            <#assign permName = Context.getMessage('label.security.labs.' + perm.name) />
        <#else>
            <#assign displayName = perm.displayName>
            ${perm.name}<#if displayName?length &gt; 0 > (${displayName})</#if>
            <#assign permName = perm.name />
        </#if>
    </div>
    <div class="span2 columns">
        <#if perm.inherited == false >
        	<button class="btn btn-small btn-danger" onClick="javascript:labsPermissionsDelete('${This.path}/@labspermissions/delete?permission=${perm.permission}&id=${perm.name}', '${Context.getMessage('label.security.labs.permission.confirm_delete', permText, permName)?js_string}');"><i class="icon-remove" style="padding-right:0px;"></i></button>
            
        <#else>
            &nbsp;
        </#if>
    </div>
</div>
</#macro>

<#macro displayPermTypeHeader permType permsList >
<#assign permText = Context.getMessage('label.security.labs.permission.' + permType) />
<div class="page-header">
    <h4>
        ${permText} 
        <small>
            <#if 0 < permsList?size >
                (${permsList?size})&nbsp;
            </#if>
            <a href="#" rel="popover" data-content="${Context.getMessage('label.security.labs.permission.' + permType + '.description')}" data-original-title="${Context.getMessage('label.security.labs.permission.' + permType)}">Description</a>
        </small>
        <div style="text-align: right;margin-top: -36px;">
            <#nested>
        </div>
    </h4>
</div>
</#macro>

<script type="text/javascript">
function vomitPublic(){
	var username='Everyone';
	if (username == undefined) {
  		alert("Pas d'utilisateur ou de groupe selectionn\u00E9");
	} else {
		var permission = 'Read';
		var operationConfirmed = false;
		var permissionText = 'Visiteur';
	    if (username == "Everyone") {
	      operationConfirmed = confirm(["DANGER: Souhaitez-vous r\u00E9ellement ajouter la permission '", permissionText, "' \u00E0 'Tout le monde' \u00E0 ce site ? Dans ce cas, votre site sera accessible par l''ensemble des collaborateurs de Leroy Merlin."].join(""));
	    } else {
	      operationConfirmed = confirm(["Souhaitez-vous r\u00E9ellement ajouter la permission '", permissionText, "' \u00E0 '", username, "' \u00E0 ce site ? Dans ce cas, votre site sera accessible par cet utilisateur."].join(""));
	    }
	    if (operationConfirmed) {
	    	jQuery('#waitingPopup').dialog2('open');
	    	jQuery.ajax({
				type: 'GET',
			    async: false,
			    url: "${This.path}/@labspermissions/haspermission?permission=" + permission +"&id=" + username,
			    success: function(data) {
			    	jQuery('#waitingPopup').dialog2('close');
			    	if (data === 'true') {
			          alert(["permission '", permissionText, "' d\u00E9j\u00E0 assign\u00E9e \u00E0 l'utilisateur '", username, "' !"].join(""));
			        }
			        else {
			          labsPermissionsHigherpermission(permission, username);
			        }
			    },
			    error: function(data){
			    	jQuery('#waitingPopup').dialog2('close');
			    }
			});
		}
	}
}

function labsPermissionsDelete(url, confirme){
	if (confirm(confirme)) {
		jQuery('#waitingPopup').dialog2('open');
		jQuery.ajax({
	        type: 'DELETE',
	        async: false,
	        url: url,
	        success: function(data, msg){
	          if (data.length > 0) {
	            alert(data);
	          }
	          else {
	            jQuery("#divDislayArray").load('${This.path}/@labspermissions');
	          }
	          jQuery('#waitingPopup').dialog2('close');
	        },
	        error: function(xhr, status, ex){
	          alert(ex);
	          jQuery('#waitingPopup').dialog2('close');
	        }
	    });
	}
}

$(function () {
		$("a[rel=popover]")
			.popover({offset: 10})
			.click(
				function(e) {e.preventDefault()}
			)
	}
)         
</script>