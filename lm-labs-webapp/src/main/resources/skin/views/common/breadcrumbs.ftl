<div id="breadcrumbs">

<#macro breadcrumb resource>
  <#if resource.document.type != "Site">
    <@breadcrumb This.previous/>
  </#if>
  <#if resource.document??>
    <a href="${resource.path}">${resource.document.title}</a> &gt;
  </#if>
</#macro>

<#if This.type.name != "sitesRoot" >
  <@breadcrumb This/>
</#if>
</div>