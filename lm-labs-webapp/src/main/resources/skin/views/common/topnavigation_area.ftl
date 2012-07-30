<#assign mySite=Common.siteDoc(Document).getSite() />

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
		<#assign topPages = Common.getTopNavigationPages(mySite.getTree(), Context.principal.name) />
		<#if topPages?size &gt; 0 >
			<#assign tabActivated = false />
			<#assign homePageId = Common.siteDoc((topPages?first).document).getSite().getHomePageRef() />
			<#list topPages as child >
				<#assign isActiveTab = false />
				<#assign pageDoc = child.document />
				<#assign url = Context.modulePath + "/" + Common.siteDoc(pageDoc).getResourcePath() />
				<#if !tabActivated >
					<#assign isActiveTab = isCurrentPage(pageDoc.id) />
					<#if isActiveTab >
						<#assign tabActivated = isActiveTab />
					</#if>
				</#if>
				<#assign childSubPages = Common.siteDoc(pageDoc).getChildrenNavigablePages(Context.principal.name) />
				<#assign title = pageDoc.title />
				<li class="<#if isActiveTab >active</#if><#if 0 < childSubPages?size > dropdown</#if>">
					<a href="${url?html}"<#if 0 < childSubPages?size > class="dropdown-toggle" data-toggle="dropdown" data-target="#" </#if>>
					<h5 class="<#if homePageId == pageDoc.id >homepage</#if>" >
					${title}<#if 0 < childSubPages?size ><b class="caret"></b></#if></h5></a>
					<#if isActiveTab >
						<div class="star"></div><#-- SC -->
					</#if>
					<#if 0 < childSubPages?size >
                        <ul class="dropdown-menu">
                            <li><a href="${url?html}" >${title}</a></li>
                            <li class="divider"></li>
                            <#list childSubPages as childSubPage >
                                <#assign url = Context.modulePath + "/" + Common.siteDoc(childSubPage.document).resourcePath />
                            <li><a href="${url?html}" >${childSubPage.document.title}</a></li>
                            </#list>
                        </ul>
					</#if>
				</li>
			</#list>
		<#else>
			<#assign url = Context.modulePath + "/" + mySite.URL />
			<li class="active"><a href="${url}"><h5>${Context.getMessage('label.homePage')}</h5></a></li>
		</#if> 
	</ul>
