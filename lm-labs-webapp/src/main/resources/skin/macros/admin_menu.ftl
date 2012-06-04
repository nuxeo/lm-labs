<#macro adminMenu item basePath="${This.path}">
	<#assign mySite=Common.siteDoc(Document).getSite() />
	<div class="container">
		<ul class="nav nav-pills">
		    <li <#if item=="general">class="active"</#if>><a href="${basePath}/@views/edit"><i class="icon-cog"></i>${Context.getMessage('label.labssite.admin.tabs.main')}</a></li>
		    <#if mySite?? && Session.hasPermission(mySite.document.ref, "Everything")>
			    <li <#if item=="theme">class="active"</#if>><a href="${basePath}/@theme/${mySite.themeManager.getTheme(Context.coreSession).name}"><i class="icon-tint"></i>${Context.getMessage('label.labssite.admin.tabs.appearance')}</a></li>
			    <li <#if item=="perms">class="active"</#if>><a href="${basePath}/@views/edit_perms"><i class="icon-asterisk"></i>${Context.getMessage('label.labssite.admin.tabs.rights')}</a></li>
    		    <li <#if item=="contacts">class="active"</#if>><a href="${basePath}/@views/edit_contacts"><i class="icon-user"></i>${Context.getMessage('label.labssite.admin.tabs.contacts')}</a></li>
		    </#if>
		    <li <#if item=="admin_page">class="active"</#if>><a href="${basePath}/@views/administer_pages"><i class="icon-file"></i>${Context.getMessage('label.labssite.admin.tabs.pages')}</a></li>
		    <li <#if item=="admin_asset">class="active"</#if>><a href="${basePath}/@views/administer_assets"><i class="icon-picture"></i>${Context.getMessage('label.labssite.admin.tabs.assets')}</a></li>
		    <#if mySite?? && Session.hasPermission(mySite.document.ref, "Everything")>
		    	<li <#if item=="trash">class="active"</#if>><a href="${basePath}/@views/edit_trash"><i class="icon-trash"></i>${Context.getMessage('label.labssite.admin.tabs.trash')}</a></li>
		    </#if>
            <li <#if item=="admin_rssfeeds">class="active"</#if>><a href="${basePath}/@views/admin_rssfeeds"><i class="icon-flag"></i>Flux RSS</a></li>
<#-- TODO
<#list This.getLinks("SITE_ADMINISTRATION_TABS") as link>
  <li><a href="${link.getCode(This)}">${Context.getMessage('label.labssite.admin.tabs.' + link.id)}</a></li>
</#list>
-->
	  	</ul>
	</div>
</#macro>