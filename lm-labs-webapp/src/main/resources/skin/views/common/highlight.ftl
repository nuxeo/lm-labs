<#assign fullTextHighlight = Context.request.getParameter('fullText') />
<#if This.trailingPath == "/@search" >
	<#-- No highlight in results page -->
	<#assign fullTextHighlight = "" />
</#if>
<#if (fullTextHighlight != null && fullTextHighlight != "") >
	$(document).ready(function() {
		$('#FKtopContent').highlight('${fullTextHighlight?js_string}');
		$('.editblock .highlight').each(function(){
			jQuery(this).replaceWith(jQuery(this).html());	
		});
	});
</#if>