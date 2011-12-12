<#macro pageStatusLabel resource>
    <#if Common.getLabsLifeCycleTypes()?seq_contains(resource.document.type) && resource.document.id == Document.id>
	    <#assign pageAdapter = resource.page />
		<#if pageAdapter.draft >
			&nbsp;<span class="label success editblock">${Context.getMessage('label.status.draft')}</span>
		<#elseif pageAdapter.deleted >
			&nbsp;<span class="label warning editblock">${Context.getMessage('label.status.deleted')}</span>
		</#if>
	</#if>
</#macro>