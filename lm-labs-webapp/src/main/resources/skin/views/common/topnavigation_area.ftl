<#function isCurrentPage url>
	<#assign longUrl = Context.modulePath + "/" + url />
	<#if This.path == longUrl>
		<#return true>
	</#if>
	<#return false>
</#function>

<div id="topPagesNavigation" >
	<ul class="tabs">
		<li class="<#if isCurrentPage(site.URL) >active</#if>" ><a href="${site.URL}">Accueil</a></li>
		<#list Common.siteDoc(site.tree).getChildrenPages() as child >
			<#if site.indexDocument.id != child.id >
				<#assign childUrl = Common.siteDoc(child).resourcePath >
			<li class="<#if isCurrentPage(childUrl) >active</#if>"><a href="${childUrl}">${child.title}</a></li>
			</#if>
		</#list>
	</ul>
</div>
