<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/searchpage.css"/>
<h1>Résultats de recherche</h1>

<#if result?size &gt; 0>
<div class="resultsSearchSubtitle">Affichage des résultats 1-${result?size} sur ${result?size} pour <span>${query?split('"')[1]}</span></div>
<style>
.sortValue {
	display: none;
}
</style>
<script type="text/javascript">
jQuery(document).ready(function() {
	jQuery("#resultsSearch").tablesorter({ 
    	sortList: [[1,0]],
    	headers: {
    		0: {
    			sorter:false
    		}
    	},
    	textExtraction: function(node) { 
            // extract data from markup and return it  
    		var sortValues = jQuery(node).find('span[class=sortValue]');
    		if (sortValues.length > 0) {
    			return sortValues[0].innerHTML;
    		}
            return node.innerHTML; 
        }
	});
});
</script>
<#--
-->
<table class="zebra-striped" id="resultsSearch" >
	<thead>
		<tr><th></th><th>Titre</th><th>Dernière Modification</th><th>Taille Fichier</th><th>Page</th></tr>
	</thead>
	<tbody>
    <#list result as doc>
    <tr>
    	<td class="colIcon"><img title="${doc.type}" alt="&gt;&gt;" <#if doc.schemas?seq_contains("common") >src="/nuxeo/${doc.common.icon}"</#if> /></td>
	    <#assign breadcrumbs = breadcrumbsDocs(doc) />
	    <#assign breadcrumbsStr = "" />
	    <#list breadcrumbs as elem>
		    <#if elem.type != "SitesRoot" && elem.type != "Site" && elem.type != "Tree" >
		    	<#assign breadcrumbsStr = breadcrumbsStr + elem.title />
		    	<#if elem.id != breadcrumbs?last.id >
		    		<#assign breadcrumbsStr = breadcrumbsStr + " &gt; " />
		    	</#if> 
		    </#if>
	    </#list>
        <td><a href="${Context.getUrlPath(doc)}" target="_blank" title="${breadcrumbsStr}" >${doc.dublincore.title}</a></td>
        <td>${userFullName(doc.dublincore.lastContributor)}</td>
        <#assign holder = This.getBlobHolder(doc) />
        <#assign formattedFilesize = "(Pas de fichier)" />
        <#assign filesize = 0 />
        <#if holder != null && holder.blob != null >
       		<#assign filesize = holder.blob.length />
        	<#assign formattedFilesize = bytesFormat(holder.blob.length, "K", "fr_FR") />
        </#if>
	    <td class="colFilesize">${formattedFilesize}<span class="sortValue">${filesize?string.computer}</span></td>
	    <#assign closestPage = This.getClosestPage(doc) />
	    <td><a href="${This.URL}${This.getPageEndUrl(closestPage)}" target="_blank" >${closestPage.title}</a></td>
    </tr>
    </#list>
    </tbody>
</table>
<#else>
<div class="resultsSearchSubtitle">Pas de résultats pour <span>${query?split('"')[1]}</span></div>
</#if>

<br/></br>
<#if Context.principal.administrator >
<small>Query performed: ${query}</small>
</#if>
