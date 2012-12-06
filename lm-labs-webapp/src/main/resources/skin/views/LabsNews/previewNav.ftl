<#include "views/LabsNews/macroNews.ftl">
<#assign pathnews = Context.modulePath + "/" + Common.siteDoc(page.document).getResourcePath() />
<div class="row-fluid">
	<@generateSummaryNews news=This.getLabsNewsAdapter(page.document) path=pathnews withHref=true/>
</div>
