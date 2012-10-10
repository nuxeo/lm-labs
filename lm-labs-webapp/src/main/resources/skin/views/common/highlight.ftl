<#assign fullTextHighlight = Context.request.getParameter('fullText') />
<#if (fullTextHighlight != null && fullTextHighlight != "") >
	$(document).ready(function() {
		$('#FKtopContent').highlight('${fullTextHighlight?js_string}');
	});
</#if>