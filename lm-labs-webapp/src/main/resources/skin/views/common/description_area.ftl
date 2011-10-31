<#if Session.hasPermission(Document.ref, 'Write') >
	<div class="well" id="pageDescription">
		<div id="description">${Document.dublincore.description}</div>
		
		<script type="text/javascript">
	      $('#description').ckeip({
	      	<#-- @put (the nuxeo way to update description) seems to not work with ckeditor
	        e_url: '${This.path}/@put', -->
	        e_url: '${This.path}/updateDescriptionCKEIP',
	        ckeditor_config: ckeditorconfig,
	        emptyedit_message: "${Context.getMessage('label.ckeditor.double_click_to_edit_content')}"
	        });
	    </script>
	</div>
<#elseif Document.dublincore.description != null>
	<div id="pageDescription">
		<div id="description">${Document.dublincore.description}</div>
	</div>
</#if>