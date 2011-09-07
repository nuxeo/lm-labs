<style>
.pageslist > ul {
	margin: 0 0 0 0.5em;
}
.pageslist > ul > li {
	display: list-item;
	font-size: 10px;
	width: 170px;
	text-overflow: ellipsis;
	white-space: nowrap;
	overflow: hidden;
}
</style>
<div class="pageslist ${pagesListClass}" >
	<div class="title">${pagesListTile}</div>
	<ul>
	<#list pageProvider.setCurrentPage(0) as page >
		<li>
			<a href="labssites/${This.siteUrlProp}${pageEndUrl(page)}" target="_blank" title="${page.title}" >${page.title}</a>
		</li>
	</#list>
	</ul>
	<a href="${detailedPageUrl}">Détails</a>
</div>

