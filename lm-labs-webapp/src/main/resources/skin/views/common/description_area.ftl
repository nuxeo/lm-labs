<div id="pageDescription">

  <h1>${Document.dublincore.description}</h1>
<#list This.getLinks("PAGE_DESCRIPTION_BEFORE_BUTTONS") as link>
    <button id="${link.id}">${Context.getMessage('command.Page.' + link.id)}</button> 
</#list>
  <div id="pageDescriptionEditDiv" style="display:none;">
    <form action="${This.path}/@put" method="POST" >
      <textarea id="dc:description" rows="2" cols="80">${Document.dublincore.description}</textarea>
    </form>
<#list This.getLinks("PAGE_DESCRIPTION_AFTER_BUTTONS") as link>
    <button id="${link.id}">${Context.getMessage('command.Page.' + link.id)}</button> 
</#list>
  </div>

<script type="text/javascript">
jQuery(document).ready(function() {
	jQuery("#pageDescriptionCancelBt").click(function(evt) {
		descriptionViewMode();
	});
	jQuery("#PageDescriptionModifyBt").click(function(evt) {
		descriptionEditMode();
	});
	jQuery("#PageDescriptionSaveBt").click(function(evt) {
		setTimeout(function() {jQuery('#waitingPopup').dialog({ modal: true });}, 100);
		jQuery.ajax({
			type: jQuery(this).closest("form").attr("method"),
			url: jQuery(this).closest("form").attr("action"),
			success: function(msg) { descriptionViewMode(); },
			error: function(msg) {
				alert("ERROR: " + msg);
			}
		});
		jQuery('#waitingPopup').dialog( "close" );
	});
	
	function descriptionViewMode() {
		jQuery('#PageDescriptionModifyBt').show();
		jQuery('#pageDescriptionEditDiv').hide():
	}
	
	function descriptionEditMode() {
		jQuery('#pageDescriptionEditDiv').show();
		jQuery('#PageDescriptionModifyBt').hide():
	}
});
</script>

</div>