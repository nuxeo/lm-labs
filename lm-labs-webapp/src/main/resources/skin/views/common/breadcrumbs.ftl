<div id="breadcrumbs">
<#if This.type.name != "sitesRoot" >
<#assign mySeq = breadcrumbsDocs(This.document) />
<#assign siteResource = mySeq?first >
<#assign siteUrlProp = siteResource.webcontainer.url >
<#assign sitePath = Context.basePath + "/labssites/" + siteUrlProp >

<#list mySeq as resource >
  <#assign sep = "&gt;" />
  <#if resource.type == "Site" >
    <a href="${sitePath}" title="${resource.dublincore.title}">${resource.dublincore.title}</a>
  <#elseif resource.type == "Tree" >
    <#assign sep = "" />
  <#else >
    <a href="${sitePath}${pageEndUrl(resource)}" title="">${resource.dublincore.title}</a>
  </#if>
  <#if mySeq?last.id != resource.id><span>${sep}</span></#if>
</#list>
</div>
</#if>