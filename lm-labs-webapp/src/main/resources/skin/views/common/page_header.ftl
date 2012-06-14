<#assign mySite=Common.siteDoc(Document).getSite() />
<#assign page = This.page />
<#if page.isDisplayable(This.DC_TITLE)>
<div class="page-title">
    <#include "views/LabsPage/page_title.ftl" >
</div>
<#else>
<div style="clear: both;" />
</#if>
<div>
	<#if page?? && page.isContributor(Context.principal.name) >
		<#include "views/common/description_area.ftl">
	<#elseif page.isDisplayable(This.DC_DESCRIPTION)>
		<div class="page-description">${page.description}</div>
	</#if>
</div>
<script type="text/javascript">
function refreshPageTitle(description) {
    //jQuery('.page-title').load('${This.path}/@views/page_title');
    jQuery('#pageHeaderTitle').attr('data-content', description);
}
</script>