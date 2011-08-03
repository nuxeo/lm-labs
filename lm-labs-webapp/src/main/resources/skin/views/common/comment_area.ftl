<div id="comment">
	<#assign param_name="description" />
	<#assign param_value=This.description />
	<#assign param_url=This.path+"/updateDescription" />
	<#assign area_width=110 />
	<#assign area_height=5 />
	<#include "views/common/wysiwyg_editor.ftl" />
</div>