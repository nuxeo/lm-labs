<#macro pageStatusLabel pageAdapter>
	<#if pageAdapter.draft >
		&nbsp;<span class="label success">${Context.getMessage('label.draft')}</span>
	<#elseif pageAdapter.deleted >
		&nbsp;<span class="label warning">${Context.getMessage('label.deleted')}</span>
	</#if>
</#macro>