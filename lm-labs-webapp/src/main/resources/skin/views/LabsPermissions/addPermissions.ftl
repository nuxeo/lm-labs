	<h1>${Context.getMessage('label.security.labs.addPermissions.title')}</h1>
	<div class="container-fluid">
		<section>
			<a href="#" rel="twipsy" data-original-title="${Context.getMessage('label.security.labs.grouporuser')}">?</a>
		 	<input type="text" id="userNamePermissions" name="userNamePermissions" value="" class="span6" style="margin-left:20px;">
			<button id="searchUsersBt" title="${Context.getMessage('command.security.searchUsers')}" class="btn primary disabled" style="margin-left:20px;" >${Context.getMessage('command.security.searchUsers')}</button>
		</section>
		<section>
			<h5>
				${Context.getMessage('label.security.labs.addPermissions.toSelect')} 
			</h5>
			<div id="divSelectedUsers">
				<#include "views/LabsPermissions/selectUsers.ftl" >
			</div>
			
		</section>
	</div>
	<div  class="actions">
		<input type="hidden" id="permissionKey" value=""/>
		<input type="hidden" id="permissionText" value=""/>
		<button id="addPerm" onClick="javascript:addPerm();" title="${Context.getMessage('command.security.addPerm')}" class="btn primary" style="margin-left:20px;" >${Context.getMessage('command.security.addPerm')}</button>
		<button id="cancel" class="btn" onClick="javascript:closeAddPerm();" title="${Context.getMessage('command.security.addPerm.cancel')}">${Context.getMessage('label.pageList.edit.manage.cancel')}</button>
	</div>

<script type="text/javascript">
jQuery(document).ready(function(){
  jQuery("#userNamePermissions").keyup(function() {
    if (jQuery(this).val().length >= 3) {
      jQuery("#searchUsersBt").removeClass('disabled');
    } else {
      jQuery("#searchUsersBt").addClass('disabled');
    }
  });
  
  jQuery("#searchUsersBt").click(function() {
    jQuery.ajax({
      type: 'GET',
      async: false,
      url: '${This.path}' + '/@labspermissions/suggestedUsers/' + jQuery("#userNamePermissions").val() ,
      success: function(data) {
      jQuery("#divSelectedUsers").html(data);
    }
    });
    return false;
  });

});

function addPerm(){
	var username=jQuery("#divSelectedUsers input:radio:checked").val();
	if (username == undefined) {
  		alert("Pas d'utilisateur ou de groupe selectionn\u00E9");
	} else {
		var permission = jQuery("#permissionKey").val();
		var operationConfirmed = false;
		var permissionText = jQuery("#permissionText").val();
	    if (username == "Everyone") {
	      operationConfirmed = confirm(["DANGER: Souhaitez-vous r\u00E9ellement ajouter la permission '", permissionText, "' \u00E0 'Tout le monde' \u00E0 ce site ? Dans ce cas, votre site sera accessible par l''ensemble des collaborateurs de Leroy Merlin."].join(""));
	    } else {
	      operationConfirmed = confirm(["Souhaitez-vous r\u00E9ellement ajouter la permission '", permissionText, "' \u00E0 '", username, "' \u00E0 ce site ? Dans ce cas, votre site sera accessible par cet utilisateur."].join(""));
	    }
	    if (operationConfirmed) {
	    	jQuery.ajax({
				type: 'GET',
			    async: false,
			    url: "${This.path}/@labspermissions/haspermission?permission=" + permission +"&id=" + username,
			    success: function(data) {
			    	if (data === 'true') {
			          alert(["permission '", permissionText, "' d\u00E9j\u00E0 assign\u00E9e \u00E0 l'utilisateur '", username, "' !"].join(""));
			        }
			        else {
			          labsPermissionsHigherpermission(permission, username);
			        }
			    }
			});
		}
	}
}

function labsPermissionsHigherpermission(permission, username){
	jQuery.ajax({
        type: 'GET',
        async: false,
        url: "${This.path}/@labspermissions/higherpermission?permission=" + permission + "&id=" + username,
        success: function(data) {
            var hashigher = (data === 'true');
            var doAdd = false;
            if (hashigher) {
            if (confirm(["L'utilisateur '", username, "' a d\u00E9j\u00E0 cette permission ou une permission sup\u00E9rieure. Voulez-vous forcer la permission choisie ?"].join("")))
              {
                doAdd = true;
              }
            } else {
              doAdd = true;
            }
            if (doAdd)
            {
              labsPermissionsAdd(username, permission);
            }
        }
    });
}

function labsPermissionsAdd(username, permission){
	jQuery.ajax({
        type: 'POST',
        async: false,
        url: "${This.path}/@labspermissions/add",
        data: { permission: permission, id: username, override: "true"  },
        success: function(data, msg){
          if (data.length > 0) {
            alert(data);
          }
          else {
            jQuery("#divDislayArray").load('${This.path}/@labspermissions');
            closeAddPerm();
          }
        },
        error: function(xhr, status, ex){
          alert(ex);
        }
    });
}

$(function () {
	$("a[rel=twipsy]")
		.twipsy({live: true})
	}
) 

</script>


	