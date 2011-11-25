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


<div class="span12" >
	<ul class="tabs">
		<#assign topPages = Common.getTopNavigationPages(site.tree, Context.principal.name) />
		<#if topPages?size &gt; 0 >
			<#list topPages as child >
				<#assign pageDoc = child.document />
				<#assign url = Context.modulePath + "/" + Common.siteDoc(pageDoc).resourcePath />
				<#assign isActiveTab = isCurrentPage(pageDoc.id) />
				<#assign title = pageDoc.title />
				<li class="<#if isActiveTab >active</#if>">
					<a href="${url}"><h5>${title}<@pageStatusLabel child /></h5></a>
					<#if isActiveTab >
						<div class="star"></div>
					</#if>
				</li>
			</#list>
		<#else>
			<#assign url = Context.modulePath + "/" + site.URL />
			<li class="active"><a href="${url}"><h5>${Context.getMessage('label.homePage')}</h5></a></li>
		</#if> 
	</ul>
</div>

