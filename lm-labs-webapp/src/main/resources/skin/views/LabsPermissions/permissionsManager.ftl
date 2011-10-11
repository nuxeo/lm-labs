<div id="divDislayArray" class="container"></div>
<div id="divSelectedUsers" class="container">
	<#include "views/LabsPermissions/selectUsers.ftl" >
</div>
<!--<div id="inputPermission" class="container" >
  <input type="hidden" id="redirectUrl" value="${Context.basePath}/portail/intralm" />
  <input type="hidden" name="action" value="grant">
  <input type="hidden" id="selectedPermissionName" value="">-->
  <div id="divSelectRights"></div>
<!--</div>-->

<script type="text/javascript">
jQuery(document).ready(function(){
  jQuery("#divDislayArray").load('${This.path}/@labspermissions');
  jQuery("#divSelectRights").load('${This.path}/@labspermissions/labsrights');
  
});

function addPerm(){
	var username=jQuery("#divSelectedUsers input:radio:checked").val();
	if (username == undefined) {
  		alert("Pas d'utilisateur ou de groupe selectionn\u00E9");
	} else {
		var permission = jQuery("#permissionName option:selected").val();
		var operationConfirmed = false;
		var permissionText = jQuery("#permissionName option:selected").text();
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
              labsPermissionsAdd(username);
            }
        }
    });
}

function labsPermissionsAdd(username){
	jQuery.ajax({
        type: 'POST',
        async: false,
        url: "${This.path}/@labspermissions/add",
        data: { permission: jQuery("#permissionName option:selected").val(), id: username, override: "true"  },
        success: function(data, msg){
          if (data.length > 0) {
            alert(data);
          }
          else {
            jQuery("#divDislayArray").load('${This.path}/@labspermissions');
          }
        },
        error: function(xhr, status, ex){
          alert(ex);
        }
    });
}

function labsPermissionsDelete(url){
	jQuery.ajax({
        type: 'DELETE',
        async: false,
        url: url,
        success: function(data, msg){
          if (data.length > 0) {
            alert(data);
          }
          else {
            jQuery("#divDislayArray").load('${This.path}/@labspermissions');
          }
        },
        error: function(xhr, status, ex){
          alert(ex);
        }
    });
}

</script>