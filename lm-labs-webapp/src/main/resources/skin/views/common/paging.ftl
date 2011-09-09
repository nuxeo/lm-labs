<#macro paging pageProvider url>
<div class="pagination">
<#--
	<h1>${pageProvider.resultsCount} ${pageProvider.numberOfPages} ${pageProvider.pageSize} ${pageProvider.currentPageIndex} ${pageProvider.currentPageStatus}</h1>
-->
	<#if pageProvider.numberOfPages &gt; 1 >
	<ul>
		<li class="first"><a href="${url}0" >&larr;&larr; ${Context.getMessage('command.paging.first')}</a></li>
		<li class="prev<#if pp.previousPageAvailable == false > disabled</#if>">
			<a href="${url}${pageProvider.currentPageIndex-1}" >&larr; ${Context.getMessage('command.paging.previous')}</a>
		</li>
		<#list 1..pageProvider.numberOfPages as p>
			<li <#if (pageProvider.currentPageIndex+1) = p>class="active"</#if> ><a href="${url}${p-1}" >${p}</a></li>
		</#list>
		<li class="next<#if pp.nextPageAvailable == false > disabled</#if>">
			<a href="${url}${pageProvider.currentPageIndex+1}" >${Context.getMessage('command.paging.next')} &rarr;</a>
		</li>
		<li class="last"><a href="${url}${pageProvider.numberOfPages-1}" >${Context.getMessage('command.paging.last')} &rarr;&rarr;</a></li>
	</ul>
	</#if>
</div>
</#macro>

<#macro resultsStatus pageProvider extraInfo="">
<#if pageProvider.resultsCount &gt; 0>
<div class="resultsStatus">Affichage des résultats ${pageProvider.currentPageOffset+1}-${pageProvider.currentPageOffset+pageProvider.currentPageSize} sur ${pageProvider.resultsCount} Page ${pageProvider.currentPageStatus} ${extraInfo}</div>
</#if>
</#macro> 