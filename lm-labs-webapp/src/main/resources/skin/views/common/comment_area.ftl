<div id="comment">
	<#assign param_name="description" />
	<#assign param_value=This.description />
	<#assign param_url=This.path+"/updateDescription" />
	<#include "views/common/wysiwyg_editor.ftl" />
</div>