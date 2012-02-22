<#assign page = This.page />
<#if page.isDisplayable(This.DC_TITLE)>
<div class="">
	<h1>
		<#if !page.isDisplayable(This.DC_DESCRIPTION) && (page.description?length > 0)>
			<span id="pageHeaderTitle" style="cursor: pointer;" rel="popover" data-content="${page.description}" data-original-title="${page.title}">${page.title}</span>
		<#else>
			${page.title}
		</#if>
	</h1>
</div>
<#else>
<div style="clear: both;" />
</#if>
<div class="">
	<#if site?? && site.isContributor(Context.principal.name) >
		<#include "views/common/description_area.ftl">
	<#elseif page.isDisplayable(This.DC_DESCRIPTION)>
		${page.description}
	</#if>
</div>
