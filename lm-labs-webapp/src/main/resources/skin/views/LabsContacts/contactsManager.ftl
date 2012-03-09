<div id="divDislayArray" class="container"></div>
<div id="divAddContacts" style="display: none;">
	<#include "views/LabsContacts/addContacts.ftl" >
</div>
</div>

<script type="text/javascript">
jQuery(document).ready(function(){
	jQuery("#divDislayArray").load('${This.path}/@labscontacts');
	initModalLabsContacts();
  
});

function initModalLabsContacts(){
	jQuery("#divAddContacts").dialog2({
		width : '580px',
		height : '250px',
		overflowy : 'auto',
		overflowx : 'hidden',
		showCloseHandle : true,
		removeOnClose : false,
		autoOpen : false
	});
}

function displayAddContact() {
	jQuery("#divAddContacts").dialog2('open');
	jQuery("#divAddContacts").dialog2("options", {title: '${Context.getMessage('label.admin.contact.addContact.title')}'});
}

function closeAddContact() {
	jQuery("#divAddContacts").dialog2('close');
}

function labsContactDelete(url, confirme){
	if (confirm(confirme)) {
		jQuery('#waitingPopup').dialog2('open');
		jQuery.ajax({
	        type: 'DELETE',
	        async: false,
	        url: url,
	        success: function(data, msg){
	          if (data.length > 0) {
	            alert(data);
	          }
	          else {
	            //jQuery("#divDislayArray").load('${This.path}/@labscontacts');
	            window.location.reload();
	          }
	        },
	        error: function(xhr, status, ex){
	          alert(ex);
	          jQuery('#waitingPopup').dialog2('close');
	        }
	    });
	}
}

$(function () {
		$("a[rel=popover]")
			.popover({offset: 10})
			.click(
				function(e) {e.preventDefault()}
			)
	}
)   
</script>