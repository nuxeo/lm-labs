<#macro pageStatusLabel pageAdapter>
	<#if !pageAdapter.isVisible() >
		&nbsp;<span class="label success">${Context.getMessage('label.draft')}</span>
	<#elseif pageAdapter.isDeleted() >
		&nbsp;<span class="label warning">${Context.getMessage('label.deleted')}</span>
	</#if>
</#macro>