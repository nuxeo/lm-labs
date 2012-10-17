<#assign fullTextHighlight = Context.request.getParameter('fullText') />
<#if This.trailingPath == "/@search" >
	<#-- No highlight in results page -->
	<#assign fullTextHighlight = "" />
</#if>
<#if (fullTextHighlight != null && fullTextHighlight != "") >
	$(document).ready(function() {
		$('#FKtopContent').highlight('${fullTextHighlight?js_string}');
	});
</#if>