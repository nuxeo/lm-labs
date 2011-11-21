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
		<#list Common.siteDoc(site.tree).getChildrenPages() as child >
			<#assign url = Context.modulePath + "/" + Common.siteDoc(child).resourcePath />
			<#assign isActiveTab = isCurrentPage(child.id) />
			<#if site.indexDocument.id != child.id >
				<#assign title = child.title />
			<#else>
				<#assign url = Context.modulePath + "/" + site.URL />
				<#assign title = Context.getMessage('label.homePage') />
				<#if This.path == url>
					<#assign isActiveTab = true />
				</#if>
			</#if>
			<li class="<#if isActiveTab >active</#if>"><a href="${url}">${title}</a></li>
		</#list>
	</ul>
</div>
