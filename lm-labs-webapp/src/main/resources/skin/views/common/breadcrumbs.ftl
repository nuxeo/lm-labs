<#assign isContributor = This.page?? && This.page.isContributor(Context.principal.name) />
<#assign showBreadcrumbs = false />
<#assign showBreadcrumbs_draftContrib = false />
<#assign parentDoc = Session.getParentDocument(Document.ref) />
<#if !mySite?? >
	<#assign mySite = Common.siteDoc(Document).site />
</#if>
<#if parentDoc.id != mySite.tree.id >
	<#assign showBreadcrumbs = true />
</#if>
<#if isContributor >
	<#if This.page.draft >
		<#assign showBreadcrumbs_draftContrib = true />
	</#if>
</#if>
<#if showBreadcrumbs || showBreadcrumbs_draftContrib >
<#include "macros/status_label.ftl" />
	<div class="container-fluid<#if showBreadcrumbs_draftContrib > editblock</#if>">
		<div class="row-fluid">
			<ul class="breadcrumb">
				<#macro breadcrumb resource>
				  <#if resource.previous?? && resource.document.type != "Site" >
				      <@breadcrumb resource.previous/>
				  </#if>
				  <#if resource.document?? && resource.document.type != "Site" >
				    <li<#if resource.document.id == Document.id> class="active"</#if>>
				    	<#if resource.document.id != Document.id>
				    		<span class="divider"><a href="${resource.path}">${resource.document.title}</a>&nbsp;&nbsp;</span>
				    	<#else>
				    		<a href="${resource.path}">${resource.document.title}</a>
				    	</#if>
				    	<#if showBreadcrumbs_draftContrib >
				    		<#assign editblockClass = "" />
				    	<#else>
				    		<#assign editblockClass = "editblock" />
				    	</#if>
		    			<@pageStatusLabel resource editblockClass=editblockClass />
					</li>
				  </#if>
				
				</#macro>
				
				<#if This.type.name != "sitesRoot" >
				  <@breadcrumb This/>
				</#if>
			</ul>
	    </div><#--  /row-fluid -->
	</div><#-- /container-fluid -->
</#if>
