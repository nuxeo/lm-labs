<div id="breadcrumbs">

<#macro breadcrumb resource>
  <#if resource.previous?? && resource.document.type != "Site" >
    <@breadcrumb resource.previous/>
  </#if>
  <#if resource.document??>
  	<#assign class = ""/>
  	<#if (Session.hasPermission(Document.ref, 'Everything') || Session.hasPermission(Document.ref, 'Write')) && resource.page ?? && !resource.page.visible>
  		<#assign class = "class='draft'"/>
  	</#if>
    <a href="${resource.path}" ${class}>${resource.document.title}</a> &gt;
  </#if>

</#macro>

<#if This.type.name != "sitesRoot" >
  <@breadcrumb This/>
</#if>
</div>