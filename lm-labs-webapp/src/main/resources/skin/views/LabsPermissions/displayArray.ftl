
<table id="sortArrayUsers" class="zebra-striped">
	<thead>
	    <tr>
    		<th class="header headerSortDown">
    			${Context.getMessage('label.security.users')} / ${Context.getMessage('label.security.groups')}
    		</th>
    		<th class="header headerSortDown">
    			${Context.getMessage('label.security.grantedPerms')}
    		</th>
    		<th class="header"></th>
    	</tr>
	</thead>
	<tbody>
		<#list permissions as perm>
			<tr>
				<td>
					<#if perm.name == 'Everyone' >
		            	${Context.getMessage('label.security.portal.' + perm.name)}
		            <#else>
		              	<#assign displayName = perm.displayName>
		             	${perm.name}<#if displayName?length &gt; 0 > (${displayName})</#if>
		            </#if>
				</td>
				<td>
					<#if perm.permission == 'Read' || perm.permission == 'Write' || perm.permission == 'Everything'>
		             	${Context.getMessage('label.security.portal.permission.' + perm.permission)}
		            <#else>
		              	${perm.permission}
		            </#if>
				</td>
				<td>
					<#if perm.inherited == false >
		              	<span onclick="javascript:labsPermissionsDelete('${This.path}/@labspermissions/delete?permission=${perm.permission}&id=${perm.name}');" style="cursor: pointer;">
		                	<img alt="${Context.getMessage('command.security.removePerm')}" src="${skinPath}/images/x.gif"/>
		              	</span>
		            </#if>
				</td>
			</tr>
		</#list>
	</tbody>
</table>
	
<div id="searchUsers" class="container" >
	<label style="width: 250px;" for="userNamePermissions">${Context.getMessage('label.security.grouporuser')}</label>
 	<input type="text" id="userNamePermissions" name="userNamePermissions" value="" style="margin-left:20px;">
	<button id="searchUsersBt" title="${Context.getMessage('command.security.searchUsers')}" class="btn primary" style="margin-left:20px;" >${Context.getMessage('command.security.searchUsers')}</button>
</div>

<script type="text/javascript">
jQuery(document).ready(function(){
  jQuery("#searchUsersBt").hide();
  jQuery("#userNamePermissions").keyup(function() {
    if (jQuery(this).val().length >= 3) {
      jQuery("#searchUsersBt").show();
    } else {
      jQuery("#searchUsersBt").hide();
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
</script>