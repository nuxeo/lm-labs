<#assign page = This.page/>
<#if page?? && Session.hasPermission(This.document.ref, "Everything")>
     <#if page.visible>
		<li><a href="#" onclick="javascript:draftPage();">${Context.getMessage('command.docactions.draft')}</a></li>
	<#else>
		<li><a href="#" onclick="javascript:publishPage();">${Context.getMessage('command.docactions.publish')}</a></li>
	</#if>
	<!--   delete     -->
	<!--<li><a href="#" onclick="javascript:deletePage();">${Context.getMessage('command.docactions.delete')}</a></li>-->
	<script type="text/javascript">
		function publishPage(){
			if (confirm("${Context.getMessage('label.lifeCycle.page.wouldYouPublish')}")){
    			jQuery.ajax({
					type: 'PUT',
				    async: false,
				    url: '${This.path}/@labspublish/publish',
				    success: function(data) {
				    	if (data == 'publish') {
				          alert("${Context.getMessage('label.lifeCycle.page.hasPublished')}");
				          document.location.href = '${This.path}';
				        }
				        else {
				          alert("${Context.getMessage('label.lifeCycle.page.hasNotPublished')}");
				        }
				    }
				});
			}
		}
		
		function draftPage(){
			if (confirm("${Context.getMessage('label.lifeCycle.page.wouldYouDraft')}")){
    			jQuery.ajax({
					type: 'PUT',
				    async: false,
				    url: '${This.path}/@labspublish/draft',
				    success: function(data) {
				    	if (data == 'draft') {
				          alert("${Context.getMessage('label.lifeCycle.page.hasDrafted')}");
				          document.location.href = '${This.path}';
				        }
				        else {
				          alert("${Context.getMessage('label.lifeCycle.page.hasNotDrafted')}");
				        }
				    }
				});
			}
		}
		
		function deletePage(){
			if (confirm("${Context.getMessage('label.lifeCycle.page.wouldYouDelete')}")){
    			jQuery.ajax({
					type: 'DELETE',
				    async: false,
				    url: '${This.path}/@labspublish/delete',
				    success: function(data) {
				    	if (data == 'delete') {
				          alert("${Context.getMessage('label.lifeCycle.page.hasDeleted')}");
				          document.location.href = '${This.previous.path}';
				        }
				        else {
				          alert("${Context.getMessage('label.lifeCycle.page.hasNotDeleted')}");
				        }
				    }
				});
			}
		}
	</script>

</#if>