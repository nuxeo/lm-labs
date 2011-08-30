<@extends src="/views/labs-base.ftl">
<#include "views/common/paging.ftl" />
<#assign nbrElemPerPage = 20 />

	<@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title} - ${Context.getMessage('title.LabsSite.latestuploads')}</@block>

	<@block name="css">
	  <@superBlock/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/latestuploads.css"/>
	</@block>

	<@block name="content">
		<div id="content">
			<h1>${Context.getMessage('title.LabsSite.latestuploads')}</h1>
			<#assign pp = latestUploadsPageProvider(Document, nbrElemPerPage) />
			<#assign currentPage = Context.request.getParameter('page')?number?long />
			<#assign uploads = pp.setCurrentPage(currentPage) />
			<@resultsStatus pageProvider=pp />
			<@paging pageProvider=pp url=This.path+"/@views/latestuploads?page=" />
			<div class="latestuploads" >
				<ul>
					<#list uploads as upload >
					<li>
						<span class="colIcon"><img title="${upload.type}" alt="&gt;" src="/nuxeo/${upload.common.icon}" /></span>
						<span class="colFileName">${upload.title}</span>
						<#assign modifDate = upload.dublincore.modified?datetime >
						<span class="colModified">${modifDate?string("EEEE dd MMMM yyyy HH:mm")}</span>
						<#assign closestPage = This.getClosestPage(upload) />
					    <span class="colClosestPage">${Context.getMessage('label.LabsSite.latestuploads.onpage')} <a href="${This.URL}${This.getPageEndUrl(closestPage)}" target="_blank" >${closestPage.title}</a></span>
					    <#assign previewUrl = This.path + pageEndUrl(upload) />
					    <div class="colActions" >
						    <span class="colView"><a href="${previewUrl}" target="_blank">${Context.getMessage('command.LabsSite.latestuploads.display')}</a></span>
						    <span class="colDownload"><a href="${previewUrl?split('/preview')[0]}">${Context.getMessage('command.LabsSite.latestuploads.download')}</a></span>
					    <div>
					</li>
					</#list>
				</ul>
			</div>
		</div>
	</@block>
</@extends>
