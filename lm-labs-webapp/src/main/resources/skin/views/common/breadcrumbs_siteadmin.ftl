<#assign mySite=Common.siteDoc(Document).getSite(Context.coreSession) />
<ul class="breadcrumb">
	<li><span class="divider"><a href="${Context.modulePath}/${mySite.URL}">${mySite.title}</a>&nbsp;&nbsp;</span></li>
	<li class="active"><a href="#">${Context.getMessage('label.labssite.admin.breadcrumbs.title')}</a></li>
</ul>
