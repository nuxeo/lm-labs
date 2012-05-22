<#macro paging pageProvider url>
<div class="pagination">
<#--
	<h1>${pageProvider.resultsCount} ${pageProvider.numberOfPages} ${pageProvider.pageSize} ${pageProvider.currentPageIndex} ${pageProvider.currentPageStatus}</h1>
-->
	<#if pageProvider.numberOfPages &gt; 1 >
	<ul>
		<#if (pageProvider.currentPageIndex - 1 > 1) >
			<li class="first"><a href="${url}0" ><i class="icon-fast-backward"></i></a></li>
		</#if>	
		<li class="prev<#if pp.previousPageAvailable == false > disabled</#if>">
			<a href="<#if pp.previousPageAvailable == true >${url}${pageProvider.currentPageIndex-1}<#else>#</#if>" ><i class="icon-backward"></i></a>
		</li>
		<#list (pageProvider.currentPageIndex - 1)..(pageProvider.currentPageIndex + 3) as p>
			<#if ((p > 0) && (p < pageProvider.numberOfPages + 1))>
				<li <#if (pageProvider.currentPageIndex+1) = p>class="active"</#if> ><a href="${url}${p-1}" >${p}</a></li>
			</#if>
		</#list>
		<li class="next<#if pageProvider.nextPageAvailable == false > disabled</#if>">
			<a href="<#if pageProvider.nextPageAvailable == true >${url}${pageProvider.currentPageIndex+1}<#else>#</#if>" ><i class="icon-forward"></i></a>
		</li>
		<#if (pageProvider.currentPageIndex < pageProvider.numberOfPages - 3) >
			<li class="last"><a href="${url}${pageProvider.numberOfPages-1}" ><i class="icon-fast-forward"></i></a></li>
		</#if>	
	</ul>
	</#if>
</div>
</#macro>

<#macro resultsStatus pageProvider extraInfo="">
<#if pageProvider.resultsCount &gt; 0>
<div class="resultsStatus">Affichage des résultats ${pageProvider.currentPageOffset+1}-${pageProvider.currentPageOffset+pageProvider.currentPageSize} sur ${pageProvider.resultsCount} Page ${pageProvider.currentPageStatus} ${extraInfo}</div>
</#if>
</#macro> 