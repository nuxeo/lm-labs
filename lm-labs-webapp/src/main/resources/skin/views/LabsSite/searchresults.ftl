<@extends src="/views/labs-base.ftl">
	
	<@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>
    
    <@block name="scripts">
	  	<@superBlock/>
<script type="text/javascript">
jQuery(document).ready(function() {
	jQuery.ajax({
		type: "GET",
		url: '${This.path}' + '/@search?fullText=' + '${Context.request.getParameter('fullText')}',
		success: function(msg) {
			jQuery('#search-result').replaceWith(msg);
		},
		error: function() { alert("ERROR"); }
	});
});
</script>
	</@block>
    
    <@block name="css">
		<@superBlock/>
		<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/searchpage.css"/>
	</@block>
	
	<@block name="content">
	<div class="content-search-result container">
		<div id="search-result">
		</div>
	</div>
	</@block>
</@extends>	