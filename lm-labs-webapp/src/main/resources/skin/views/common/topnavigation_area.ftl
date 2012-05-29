<#assign mySite=Common.siteDoc(Document).getSite(Context.coreSession) />

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

	<ul class="nav nav-tabs topnavpages">
		<#assign topPages = Common.getTopNavigationPages(mySite.getTree(Context.coreSession), Context.principal.name) />
		<#if topPages?size &gt; 0 >
			<#assign tabActivated = false />
			<#assign homePageId = Common.siteDoc((topPages?first).document).getSite(Context.coreSession).getHomePageRef() />
			<#list topPages as child >
				<#assign isActiveTab = false />
				<#assign pageDoc = child.document />
				<#assign url = Context.modulePath + "/" + Common.siteDoc(pageDoc).getResourcePath(Context.coreSession) />
				<#if !tabActivated >
					<#assign isActiveTab = isCurrentPage(pageDoc.id) />
					<#if isActiveTab >
						<#assign tabActivated = isActiveTab />
					</#if>
				</#if>
				<#assign title = pageDoc.title />
				<li class="<#if isActiveTab >active</#if>">
					<a href="${url}"><h5 class="<#if homePageId == pageDoc.id >homepage</#if>" >
					${title}</h5></a>
					<#if isActiveTab >
						<div class="star"></div>
					</#if>
				</li>
			</#list>
		<#else>
			<#assign url = Context.modulePath + "/" + mySite.URL />
			<li class="active"><a href="${url}"><h5>${Context.getMessage('label.homePage')}</h5></a></li>
		</#if> 
	</ul>
