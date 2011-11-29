<#include "macros/status_label.ftl" />
	<ul class="breadcrumb">
		<#macro breadcrumb resource>
		  <#if resource.previous?? && resource.document.type != "Site" >
		    <@breadcrumb resource.previous/>
		  </#if>
		  <#if resource.document??>
		  	<#assign class = ""/>
		    <li><a href="${resource.path}">${resource.document.title}
		    <#if resource.document.type != "Site" && resource.document.type != "SiteTheme" && resource.document.id == Document.id>
	    		<@pageStatusLabel resource.page />
		    </#if>
		    </a> <span class="divider">&gt;</span></li>
		  </#if>
		
		</#macro>
		
		<#if This.type.name != "sitesRoot" >
		  <@breadcrumb This/>
		</#if>
	</ul>
