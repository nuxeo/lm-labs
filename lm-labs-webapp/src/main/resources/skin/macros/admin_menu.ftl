<#macro adminMenu item basePath="${This.path}">
	<div class="container">
		<ul class="pills">
		    <li <#if item=="general">class="active"</#if>><a href="${basePath}/@views/edit">Général</a></li>
		    <li <#if item=="theme">class="active"</#if>><a href="${basePath}/theme/${site.themeManager.theme.name}">Thème</a></li>
		    <li <#if item=="perms">class="active"</#if>><a href="${basePath}/@views/edit_perms">Permissions</a></li>
		    <li <#if item=="admin_page">class="active"</#if>><a href="${basePath}/@views/administer_pages">Gérer les Pages</a></li>
		    <li <#if item=="admin_asset">class="active"</#if>><a href="${basePath}/@views/administer_assets">Gérer les médias</a></li>
		    <li <#if item=="trash">class="active"</#if>><a href="${basePath}/@views/edit_trash">Poubelle</a></li>
	  	</ul>
	</div>
</#macro>