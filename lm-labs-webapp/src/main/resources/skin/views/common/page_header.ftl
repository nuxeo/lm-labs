<#assign page = This.page />
<#if page.isDisplayable(This.DC_TITLE)>
<div class="page-title">
    <#include "views/LabsPage/page_title.ftl" >
</div>
<#else>
<div style="clear: both;" />
</#if>
<div class="page-description">
	<#if site?? && site.isContributor(Context.principal.name) >
		<#include "views/common/description_area.ftl">
	<#elseif page.isDisplayable(This.DC_DESCRIPTION)>
		${page.description}
	</#if>
</div>
<script type="text/javascript">
function refreshPageTitle(description) {
    //jQuery('.page-title').load('${This.path}/@views/page_title');
    jQuery('#pageHeaderTitle').attr('data-content', description);
}
</script>