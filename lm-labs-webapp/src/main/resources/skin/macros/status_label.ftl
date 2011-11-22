<#macro pageStatusLabel pageAdapter>
	<#if pageAdapter.draft >
		&nbsp;<span class="label success">${Context.getMessage('label.status.draft')}</span>
	<#elseif pageAdapter.deleted >
		&nbsp;<span class="label warning">${Context.getMessage('label.status.deleted')}</span>
	</#if>
</#macro>