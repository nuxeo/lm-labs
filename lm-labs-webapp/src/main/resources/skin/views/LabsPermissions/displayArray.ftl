<#assign permName = "" />
<section>
	<div class="page-header">
		<#assign permText = Context.getMessage('label.security.labs.permission.Everything') />
		<h4>
			${permText} 
			<small>
				<#if 0 < permissionsAdmin?size >
					(${permissionsAdmin?size})&nbsp;
				</#if>
				<a href="#" rel="popover" data-content="${Context.getMessage('label.security.labs.permission.Everything.description')}" data-original-title="${Context.getMessage('label.security.labs.permission.Everything')}">Description</a>
			</small>
			<div style="text-align: right;margin-top: -37px;">
				<button id="displayAddPerm" onClick="javascript:displayAddPerm('${Context.getMessage('label.security.labs.permission.Everything')?js_string}', 'Everything');" title="${Context.getMessage('label.security.labs.button.add') + ' ' + Context.getMessage('label.security.labs.permission.Everything')}" class="btn" style="margin-left:20px;" >+</button>
			</div>
		</h4>
	</div>
	<#list permissionsAdmin as perm>
		<div class="row">
			<div class="span2 columns">
				&nbsp;
			</div>
			<div class="span12 columns">
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
	              	<span onclick="javascript:labsPermissionsDelete('${This.path}/@labspermissions/delete?permission=${perm.permission}&id=${perm.name}', '${Context.getMessage('label.security.labs.permission.confirm_delete', permText, permName)?js_string}');" style="cursor: pointer;">
	                	<img alt="${Context.getMessage('command.security.removePerm')}" src="${skinPath}/images/x.gif"/>
	              	</span>
	            <#else>
	            	&nbsp;
	            </#if>
	        </div>
		</div>
	</#list>
</section>

<section>
	<div class="page-header">
		<#assign permText = Context.getMessage('label.security.labs.permission.Write') />
		<h4>
			${permText} 
			<small>
				<#if 0 < permissionsWrite?size >
					(${permissionsWrite?size})&nbsp;
				</#if>
				<a href="#" rel="popover" data-content="${Context.getMessage('label.security.labs.permission.Write.description')}" data-original-title="${Context.getMessage('label.security.labs.permission.Write')}">Description</a>
			</small>
			<div style="text-align: right;margin-top: -37px;">
				<button id="displayAddPerm" onClick="javascript:displayAddPerm('${Context.getMessage('label.security.labs.permission.Write')?js_string}', 'ReadWrite');" title="${Context.getMessage('label.security.labs.button.add') + ' ' + Context.getMessage('label.security.labs.permission.Write')}" class="btn " style="margin-left:20px;" >+</button>
			</div>
		</h4>
	</div>
	<#list permissionsWrite as perm>
		<div class="row">
			<div class="span2 columns">
				&nbsp;
			</div>
			<div class="span12 columns">
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
	              	<span onclick="javascript:labsPermissionsDelete('${This.path}/@labspermissions/delete?permission=${perm.permission}&id=${perm.name}', '${Context.getMessage('label.security.labs.permission.confirm_delete', permText, permName)?js_string}');" style="cursor: pointer;">
	                	<img alt="${Context.getMessage('command.security.removePerm')}" src="${skinPath}/images/x.gif"/>
	              	</span>
	            <#else>
	            	&nbsp;
	            </#if>
	        </div>
		</div>
	</#list>
</section>

<section>
	<div class="page-header">
		<#assign permText = Context.getMessage('label.security.labs.permission.Read') />
		<h4>
			${permText} 
			<small>
				<#if 0 < permissionsRead?size >
					(${permissionsRead?size})&nbsp;
				</#if>
				<a href="#" rel="popover" data-content="${Context.getMessage('label.security.labs.permission.Read.description')}" data-original-title="${Context.getMessage('label.security.labs.permission.Read')}">Description</a>
			</small>
			<div style="text-align: right;margin-top: -37px;">
				<button id="vomitPublic" onClick="javascript:vomitPublic();" title="${Context.getMessage('label.security.labs.button.vomitPublic')}" class="btn">${Context.getMessage('label.security.labs.button.vomitPublic')}</button>
				<button id="displayAddPerm" onClick="javascript:displayAddPerm('${Context.getMessage('label.security.labs.permission.Read')?js_string}', 'Read');" title="${Context.getMessage('label.security.labs.button.add') + ' ' + Context.getMessage('label.security.labs.permission.Read')}" class="btn">+</button>
			</div>
		</h4>
	</div>
	<#list permissionsRead as perm>
		<div class="row">
			<div class="span2 columns">
				&nbsp;
			</div>
			<div class="span12 columns">
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
	              	<span onclick="javascript:labsPermissionsDelete('${This.path}/@labspermissions/delete?permission=${perm.permission}&id=${perm.name}', '${Context.getMessage('label.security.labs.permission.confirm_delete', permText, permName)?js_string}');" style="cursor: pointer;">
	                	<img alt="${Context.getMessage('command.security.removePerm')}" src="${skinPath}/images/x.gif"/>
	              	</span>
	            <#else>
	            	&nbsp;
	            </#if>
	        </div>
		</div>
	</#list>
</section>

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
	    	jQuery.ajax({
				type: 'GET',
			    async: false,
			    url: "${This.path}/@labspermissions/haspermission?permission=" + permission +"&id=" + username,
			    success: function(data) {
			    	if (data === 'true') {
			          alert(["permission '", permissionText, "' d\u00E9j\u00E0 assign\u00E9e \u00E0 l'utilisateur '", username, "' !"].join(""));
			        }
			        else {
			          labsPermissionsHigherpermission(permission, username);
			        }
			    }
			});
		}
	}
}

function labsPermissionsDelete(url, confirme){
	if (confirm(confirme)) {
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
	        },
	        error: function(xhr, status, ex){
	          alert(ex);
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