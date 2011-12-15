<#macro adminMenu item basePath="${This.path}">
	<div class="container">
		<ul class="pills">
		    <li <#if item=="general">class="active"</#if>><a href="${basePath}/@views/edit">${Context.getMessage('label.labssite.admin.tabs.main')}</a></li>
		    <#if site?? && Session.hasPermission(site.document.ref, "Everything")>
			    <li <#if item=="theme">class="active"</#if>><a href="${basePath}/@theme/${site.themeManager.theme.name}">${Context.getMessage('label.labssite.admin.tabs.appearance')}</a></li>
			    <li <#if item=="perms">class="active"</#if>><a href="${basePath}/@views/edit_perms">${Context.getMessage('label.labssite.admin.tabs.rights')}</a></li>
		    </#if>
		    <li <#if item=="admin_page">class="active"</#if>><a href="${basePath}/@views/administer_pages">${Context.getMessage('label.labssite.admin.tabs.pages')}</a></li>
		    <li <#if item=="admin_asset">class="active"</#if>><a href="${basePath}/@views/administer_assets">${Context.getMessage('label.labssite.admin.tabs.assets')}</a></li>
		    <#if site?? && Session.hasPermission(site.document.ref, "Everything")>
		    	<li <#if item=="trash">class="active"</#if>><a href="${basePath}/@views/edit_trash">${Context.getMessage('label.labssite.admin.tabs.trash')}</a></li>
		    </#if>
<#-- TODO
<#list This.getLinks("SITE_ADMINISTRATION_TABS") as link>
  <li><a href="${link.getCode(This)}">${Context.getMessage('label.labssite.admin.tabs.' + link.id)}</a></li>
</#list>
-->
	  	</ul>
	</div>
</#macro>