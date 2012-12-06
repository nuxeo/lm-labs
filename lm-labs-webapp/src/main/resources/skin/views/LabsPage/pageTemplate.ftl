<#if (pages?size > 0)>
	<ul class="dropdown-menu assistant" role="menu" aria-labelledby="dropdownMenu" style="width: 100%">
		<#list pages as child >
		  <li><a tabindex="-1" refPage="${child.document.id}" onClick="javascript:loadPreviewTemplate('<#if child.hasElementPreview() >${This.getPathBlobPreview(child.document)}/@blob<#else>noPreview</#if>', '${skinPath}', this);" href="#"><i class="icon-arrow-right"></i>${child.title}</a></li>
		</#list>
	</ul>
<#else>
<div style="width: 100%;text-align: center;margin-top: 20px;">
	<h4>Pas de modèle <small>pour cette catégorie</small></h4>
</div>
</#if>