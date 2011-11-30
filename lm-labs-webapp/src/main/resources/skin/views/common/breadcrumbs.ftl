<#include "macros/status_label.ftl" />
	<ul class="breadcrumb">
		<#macro breadcrumb resource>
		  <#if resource.previous?? && resource.document.type != "Site" >
		    <@breadcrumb resource.previous/>
		  </#if>
		  <#if resource.document??>
		    <li<#if resource.document.id == Document.id> class="active"</#if>><a href="${resource.path}">${resource.document.title}
		    </a>
    		<@pageStatusLabel resource />
		    <#if resource.document.id != Document.id>
		    <span class="divider">/</span></li>
		    </#if>
		  </#if>
		
		</#macro>
		
		<#if This.type.name != "sitesRoot" >
		  <@breadcrumb This/>
		</#if>
	</ul>
