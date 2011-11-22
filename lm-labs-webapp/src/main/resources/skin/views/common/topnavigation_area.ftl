<#include "macros/status_label.ftl" />
<#function isCurrentPage id>
	<#if id == Document.id >
		<#return true>
	</#if>
	<#assign ancestors = Session.getParentDocuments(Document.ref) />
	<#list ancestors?reverse as page>
		<#if page.type == "Tree">
			<#break>
		</#if>
		<#if id == page.id >
			<#return true>
		</#if>
	</#list>
	<#return false>
</#function>

<div id="topPagesNavigation" >
	<ul class="tabs">
		<#assign topPages = Common.getTopNavigationPages(site.tree, Context.principal.name) />
		<#if topPages?size &gt; 0 >
			<#list topPages as child >
					<#assign pageDoc = child.document />
					<#assign url = Context.modulePath + "/" + Common.siteDoc(pageDoc).resourcePath />
					<#assign isActiveTab = isCurrentPage(pageDoc.id) />
					<#if site.indexDocument.id != pageDoc.id >
						<#assign title = pageDoc.title />
					<#else>
						<#assign url = Context.modulePath + "/" + site.URL />
						<#assign title = Context.getMessage('label.homePage') />
						<#if This.path == url>
							<#assign isActiveTab = true />
						</#if>
					</#if>
					<li class="<#if isActiveTab >active</#if>"><a href="${url}">${title}<@pageStatusLabel child /></a></li>
			</#list>
		<#else>
			<#assign url = Context.modulePath + "/" + site.URL />
			<li class="active"><a href="${url}">${Context.getMessage('label.homePage')}</a></li>
		</#if> 
	</ul>
</div>
