	<h1>${Context.getMessage('label.admin.contact.addContact.title')}</h1>
	<div class="container-fluid">
		<section>
			<a href="#" rel="tooltip" data-original-title="${Context.getMessage('label.security.labs.grouporuser')}">?</a>
		 	<input type="text" id="usernameContacts" name="usernameContacts" value="" class="span4" style="margin-left:20px;">
			<button id="searchUsersBt" title="${Context.getMessage('command.security.searchUsers')}" class="btn btn-primary disabled" style="margin-left:20px;" >${Context.getMessage('command.security.searchUsers')}</button>
		</section>
		<section>
			<h5>${Context.getMessage('label.admin.contact.addContact.toSelect')}</h5>
			<div id="divSelectedUsers">
				<#include "views/LabsContacts/selectUsers.ftl" >
			</div>
		</section>
	</div>
	<div  class="actions">
		<button id="addPerm" onClick="javascript:addContact();" title="${Context.getMessage('command.contact.add')}" class="btn btn-primary" style="margin-left:20px;" >${Context.getMessage('command.contact.add')}</button>
		<button id="cancel" class="btn" onClick="javascript:closeAddContact();" title="${Context.getMessage('command.contact.cancel')}">${Context.getMessage('command.contact.cancel')}</button>
	</div>

<script type="text/javascript">
jQuery(document).ready(function(){
  jQuery("#usernameContacts").keyup(function() {
    if (jQuery(this).val().length >= 3) {
      jQuery("#searchUsersBt").removeClass('disabled');
    } else {
      jQuery("#searchUsersBt").addClass('disabled');
    }
  });
  
  jQuery("#searchUsersBt").click(function() {
  	jQuery('#waitingPopup').dialog2('open');
    jQuery.ajax({
      type: 'GET',
      async: false,
      url: '${This.path}' + '/@labscontacts/suggestedUsers/' + jQuery("#usernameContacts").val() ,
      success: function(data) {
	      jQuery("#divSelectedUsers").html(data);
	      jQuery('#waitingPopup').dialog2('close');
	  },
      error: function(data) {
      	alert('ERROR' + data.responseText);
	    jQuery('#waitingPopup').dialog2('close');
	  }
    });
    return false;
  });

});

function addContact(){
	var username=jQuery("#divSelectedUsers input:radio:checked").val();
	if (username == undefined) {
  		alert("Pas d'utilisateur selectionn\u00E9");
	} else {
		jQuery('#waitingPopup').dialog2('open');
    	jQuery.ajax({
			type: 'GET',
		    async: false,
		    url: "${This.path}/@labscontacts/add?id=" + username,
		    success: function(data) {
		    	closeAddContact();
		    	//jQuery("#divDislayArray").load('${This.path}/@labscontacts');
		    	window.location.reload();
		    },
		      error: function(data) {
		      	alert('ERROR' + data.responseText);
			    jQuery('#waitingPopup').dialog2('close');
			  }
		});
	}
}

$(function () {
	$("a[rel=tooltip]")
		.tooltip({live: true}) // TODO live still exists in 'tooltip' ??
	}
) 

</script>


	