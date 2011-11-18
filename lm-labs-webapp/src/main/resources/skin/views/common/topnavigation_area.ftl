<div id="topPagesNavigation" >
	<ul class="tabs">
		<li><a href="${site.URL}">Accueil</a></li>
	<#list Common.siteDoc(site.tree).getChildrenPages() as child >
		<li><a href="${Common.siteDoc(child).resourcePath}">${child.title}</a></li>
	</#list>
	</ul>
</div>