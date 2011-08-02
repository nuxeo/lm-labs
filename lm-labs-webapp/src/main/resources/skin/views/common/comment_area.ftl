<div id="comment">
	Bienvenue sur le site '${This.name}'
	<br />
	<#assign param_name="description" />
	<#assign param_value=This.description />
	<#assign param_url=This.path+"/updateDescription" />
	<#include "views/common/wysiwyg_editor.ftl" />
</div>