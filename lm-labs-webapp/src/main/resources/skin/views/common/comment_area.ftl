<div id="comment">
<#if Document.type == "Site">
	Bienvenue sur le site '${This.name}'
	<br />
</#if>
	<#assign param_name="description" />
	<#assign param_value=This.description />
	<#assign param_url=This.path+"/updateDescription" />
	<#include "views/common/wysiwyg_editor.ftl" />
</div>