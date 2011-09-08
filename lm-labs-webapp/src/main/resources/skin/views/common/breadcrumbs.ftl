<div id="breadcrumbs">
<#if This.type.name != "sitesRoot" >
<#assign breadcrumbs = breadcrumbsDocs(This.document) />
<#assign sitePath = Context.basePath + "/labssites/" + breadcrumbs?first.webcontainer.url >

<#list breadcrumbs as resource >
  <#assign sep = "&gt;" />
  <#if resource.type == "Site" >
    <a href="${sitePath}" title="${resource.dublincore.title}">${resource.dublincore.title}</a>
  <#elseif resource.type == "Tree" >
    <#assign sep = "" />
  <#else >
    <a href="${sitePath}${pageEndUrl(resource)}" title="">${resource.dublincore.title}</a>
  </#if>
  <#if breadcrumbs?last.id != resource.id><span>${sep}</span></#if>
</#list>
</div>
</#if>