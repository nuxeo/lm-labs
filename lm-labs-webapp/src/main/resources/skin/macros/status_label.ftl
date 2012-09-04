<#macro pageStatusLabel resource editblockClass="editblock" >
    <#if Common.getLabsLifeCycleTypes()?seq_contains(resource.document.type) && resource.document.id == Document.id>
	    <#assign pageAdapter = resource.page />
		<#if pageAdapter.draft >
			&nbsp;<span class="label label-success ${editblockClass}" style="cursor: pointer;" onClick="javascript:publishPage();" title="Publier">${Context.getMessage('label.status.draft')}</span>
		<#elseif pageAdapter.deleted >
			&nbsp;<span class="label label-warning ${editblockClass}">${Context.getMessage('label.status.deleted')}</span>
		</#if>
		<#if pageAdapter.hiddenInNavigation >
			&nbsp;<span class="label label-inverse ${editblockClass}">${Context.getMessage('label.status.hiddenInNavigation')}</span>
		</#if>
	</#if>
</#macro>