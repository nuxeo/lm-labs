<div id="breadcrumbs">
<#assign mySeq = [] />
<@prev doc = This.document />

<#macro prev doc>
  <#if doc.type == "SitesRoot" >
    <#return>
  </#if>
  <#if doc.type != "Tree" >
    <#assign mySeq = mySeq + [doc] />
  </#if>
  <@prev doc = Session.getParentDocument(doc.ref) /> 
</#macro>

<#assign siteResource = mySeq?last >
<#assign siteUrlProp = siteResource.webcontainer.url >
<#assign sitePath = Context.basePath + "/labssites/" + siteUrlProp >

<#list mySeq?reverse as resource >
  <#if resource.type == "Site" >
    <a href="${sitePath}" title="${resource.dublincore.title}">${resource.dublincore.title}</a>
  <#else>
    <a href="${sitePath}/id/${resource.id}" title="">${resource.dublincore.title}</a>
  </#if>
  <#if mySeq?first.id != resource.id><span>&gt;</span></#if>
</#list>
</div>