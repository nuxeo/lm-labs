<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/searchpage.css"/>
<h1>Résultats de recherche</h1>

<#if result?size &gt; 0>
<div class="resultsSearchSubtitle">Affichage des résultats 1-${result?size} sur ${result?size} pour <span>${query?split('"')[1]}</span></div>

<ul>
    <#list result as doc>
    <li>
    	<span class="colIcon"><img title="${doc.type}" alt="&gt;" src="/nuxeo/${doc.common.icon}" /></span>
        <span><a href="${Context.getUrlPath(doc)}" target="_blank" >${doc.dublincore.title}</a></span>
        <span>par ${doc.dublincore.lastContributor}</span>
        <#assign filesize = "No File" />
        <#assign holder = This.getBlobHolder(doc) />
        <#if holder != null && holder.blob != null >
        	<#assign filesize = bytesFormat(holder.blob.length, "K", "fr_FR") />
        </#if>
	    <br/>
	    <span class="colFilesize">${filesize}</span>
	    <#assign closestPage = This.getClosestPage(doc) />
	    <span>sur page <a href="${This.URL}${This.getPageEndUrl(closestPage)}" target="_blank" >${closestPage.title}</a></span>
	    <span>
	    <br/>
	    <#assign breadcrumbs = breadcrumbsDocs(doc) />
	    <#list breadcrumbs as elem>
	    <#if elem.type != "SitesRoot" && elem.type != "Site" && elem.type != "Tree" >
	    ${elem.title}
	    	<#if elem.id != breadcrumbs?last.id >
	    	&gt;
	    	</#if> 
	    </#if>
	    </#list>
	    </span>
    </li>
    </#list>
</ul>
<#else>
<div class="resultsSearchSubtitle">Pas de résultats pour <span>${query?split('"')[1]}</span></div>
</#if>

<br/></br>
<#if Context.principal.administrator >
<small>Query performed: ${query}</small>
</#if>

<#function parentPage doc >
</#function>
