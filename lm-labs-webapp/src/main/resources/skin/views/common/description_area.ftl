<div id="pageDescription">

  <h1>${Document.dublincore.description}</h1>
<#list This.getLinks("PAGE_DESCRIPTION_BEFORE_BUTTONS") as link>
    <button id="${link.id}">${Context.getMessage('command.Page.' + link.id)}</button> 
</#list>
  <div id="pageDescriptionEditDiv" style="display:none;">
    <form id="pageDescriptionForm" action="${This.path}/@put" method="POST" >
      <textarea id="dc:description" rows="2" cols="80">${Document.dublincore.description}</textarea>
    </form>
<#list This.getLinks("PAGE_DESCRIPTION_AFTER_BUTTONS") as link>
    <button id="${link.id}">${Context.getMessage('command.Page.' + link.id)}</button> 
</#list>
  </div>

	<script type="text/javascript">
	function descriptionViewMode() {
		jQuery('#PageDescriptionModifyBt').show();
		jQuery('#pageDescriptionEditDiv').hide();
	}
	function descriptionEditMode() {
		jQuery('#pageDescriptionEditDiv').show();
		jQuery('#PageDescriptionModifyBt').hide();
	}
	jQuery(document).ready(function() {
		jQuery("#PageDescriptionCancelBt").click(function(evt) {
			descriptionViewMode();
		});
		jQuery("#PageDescriptionModifyBt").click(function(evt) {
			descriptionEditMode();
		});
		jQuery("#PageDescriptionSaveBt").click(function(evt) {
			jQuery.ajax({
				type: jQuery("#pageDescriptionForm").attr("method"),
				url: jQuery("#pageDescriptionForm").attr("action"),
				success: function(msg) {
				jQuery('#pageDescription>h1').val(jQuery("dc:description").val());
					descriptionViewMode();
				},
				error: function(msg) {
					alert("ERROR: " + msg);
				}
			});
		});
	});
	</script>

</div>