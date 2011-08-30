<#macro paging pageProvider url>
<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/paging.css"/>
<div class="paging">
<#--
	<h1>${pageProvider.resultsCount} ${pageProvider.numberOfPages} ${pageProvider.pageSize} ${pageProvider.currentPageIndex} ${pageProvider.currentPageStatus}</h1>
-->
	<#if pageProvider.numberOfPages &gt; 1 >
		<ul>
		<li><span class="pagingFirst">
			<a href="${url}0" title="${Context.getMessage('command.paging.first')}" >${Context.getMessage('command.paging.first')}</a>
		</span></li>
		<li><span class="pagingPrevious">
		<#if pp.previousPageAvailable >
			<a href="${url}${pageProvider.currentPageIndex-1}" title="${Context.getMessage('command.paging.previous')}" >${Context.getMessage('command.paging.previous')}</a>
		</#if>
		</span></li>
		<#list 1..pageProvider.numberOfPages as p>
			<li><span class="pagingPageNumber">
			<#if (pageProvider.currentPageIndex+1) = p>
				${p}
			<#else>
				<a href="${url}${p-1}" >${p}</a>
			</#if>
			</span></li>
		</#list>
		<li><span class="pagingNext">
		<#if pp.nextPageAvailable >
			<a href="${url}${pageProvider.currentPageIndex+1}" title="${Context.getMessage('command.paging.next')}" >${Context.getMessage('command.paging.next')}</a>
		</#if>
		</span></li>
		<li><span class="pagingLast"><a href="${url}${pageProvider.numberOfPages-1}" title="${Context.getMessage('command.paging.last')}" >${Context.getMessage('command.paging.last')}</a>
		</span></li>
		</ul>
	</#if>
</div>
</#macro>

<#macro resultsStatus pageProvider extraInfo="">
<#if pageProvider.resultsCount &gt; 0>
<div class="resultsStatus">Affichage des résultats ${pageProvider.currentPageOffset+1}-${pageProvider.currentPageOffset+pageProvider.currentPageSize} sur ${pageProvider.resultsCount} Page ${pageProvider.currentPageStatus} ${extraInfo}</div>
</#if>
</#macro> 