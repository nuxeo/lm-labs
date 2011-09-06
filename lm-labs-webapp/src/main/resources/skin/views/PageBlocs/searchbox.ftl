<div id="searchbox" class="sidebarzone" >
	<input type="text" id="fullText" name="fullText" />
	<img id="searchBt" src="${skinPath}/images/search.png" />
</div>

<script type="text/javascript">
jQuery(document).ready(function() {
	var adapter = '/@search?fullText=';
	//var adapter = '/@views/searchpage?fullText=';
	jQuery('#searchBt').click(function() {
		jQuery.ajax({
			type: "GET",
			url: '${This.previous.path}' + adapter + jQuery('#fullText').val(),
			success: function(msg) {
				jQuery('div.content').html(msg);
			},
			error: function(xhr, status, error) { alert("ERROR: " + xhr + "/" + error); }
		});
	});
});
</script>
