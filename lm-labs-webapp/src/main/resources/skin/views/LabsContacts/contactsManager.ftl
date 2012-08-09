<div id="divDislayArray" class="container"><img src="${skinPath}/images/loading.gif" /></div>
<div id="divAddContacts" style="display: none;">
	<#include "views/LabsContacts/addContacts.ftl" >
</div>
</div>

<script type="text/javascript">
jQuery(document).ready(function(){
	initModalLabsContacts();
	loadContacts();
});

function loadContacts(){
	jQuery("#divDislayArray")[0].innerHTML = '<img src="${skinPath}/images/loading.gif" />';
	jQuery.ajax({
        type: 'GET',
        async: false,
        url: '${This.path}/@labscontacts',
        success: function(data, msg){
          jQuery("#divDislayArray").html(data);
        },
        error: function(xhr, status, ex){
          alert(ex);
        }
    });
}

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
	            window.location.reload();
	          }
	         	jQuery('#waitingPopup').dialog2('close');
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