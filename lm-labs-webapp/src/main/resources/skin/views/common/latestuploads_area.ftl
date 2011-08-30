<style>
#latestuploads > ul {
	margin: 0 0 0 0.5em;
}
#latestuploads > ul > li {
	display: list-item;
	font-size: 10px;
	width: 150px;
}
</style>
<div id="latestuploads" class="sidebarzone" >
	<div class="title">${Context.getMessage('title.LabsSite.latestuploads')}</div>
	<ul>
	<#assign pp = latestUploadsPageProvider(Document, 5) />
	<#list pp.setCurrentPage(0) as upload >
		<li>
			<a href="labssites/${This.siteUrlProp}${pageEndUrl(upload)}" target="_blank" title="${upload.title}" >${upload.title}</a>
		</li>
	</#list>
	</ul>
	<a href="${This.previous.path}/@views/latestuploads?page=0">Détails</a>
</div>

