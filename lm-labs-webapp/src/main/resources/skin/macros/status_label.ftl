<#macro pageStatusLabel resource>
    <#if Common.getLabsLifeCycleTypes()?seq_contains(resource.document.type) && resource.document.id == Document.id>
	    <#assign pageAdapter = resource.page />
		<#if pageAdapter.draft >
			&nbsp;<span class="label label-success editblock" style="cursor: pointer;" onClick="javascript:publishPage();" title="Publier">${Context.getMessage('label.status.draft')}</span>
		<#elseif pageAdapter.deleted >
			&nbsp;<span class="label label-warning editblock">${Context.getMessage('label.status.deleted')}</span>
		</#if>
	</#if>
</#macro>