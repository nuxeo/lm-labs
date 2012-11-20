<ul class="dropdown-menu assistant" role="menu" aria-labelledby="dropdownMenu">
	<#list pages as child >
	  <li><a tabindex="-1" refPage="${child.document.id}" onClick="javascript:loadPreviewTemplate('<#if child.hasElementPreview() >${This.getPathBlobPreview(child.document)}/@blob<#else>noPreview</#if>', '${skinPath}', this);" href="#"><i class="icon-arrow-right"></i>${child.title}</a></li>
	</#list>
</ul>