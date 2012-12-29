<#macro generateExternalLinksList >
<#list Common.siteDoc(Document).site.externalURLs as e >
	<li><a href="${e.URL}" style="word-wrap: break-word;" target="_blank" title="${e.URL}">${e.name}</a></li>
</#list>
</#macro>