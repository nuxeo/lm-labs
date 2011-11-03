<#assign page = This.page/>
<#if page?? && Session.hasPermission(This.document.ref, "Everything")>
     <#if page.visible>
		<li><a href="#" onclick="javascript:draftPage();">${Context.getMessage('command.docactions.draft')}</a></li>
	<#else>
		<li><a href="#" onclick="javascript:publishPage();">${Context.getMessage('command.docactions.publish')}</a></li>
	</#if>
	<!--   delete     -->
	<li><a href="#" onclick="javascript:deletePage();">${Context.getMessage('command.docactions.delete')}</a></li>
	<!--   Manage parameter's page     -->
	<li><a href="#" onclick="javascript:openParametersPage();">${Context.getMessage('command.docactions.parameters')}</a></li>
	<div id="divEditParametersPage" style="display: none;">
		<#include "views/common/page_parameters.ftl" />
	</div>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			jQuery("#divEditParametersPage").dialog2({
				autoOpen : false,
				closeOnOverlayClick : false,
				removeOnClose : false,
				showCloseHandle : false,
			});
		});
		
		var divEditParametersPageForm;
		
		function openParametersPage(){
			jQuery("#divEditParametersPage").dialog2('open');
			divEditParametersPageForm = jQuery("#divEditParametersPage")[0].innerHTML;
		}
		
		function closeParametersPage(){
			jQuery("#divEditParametersPage").dialog2('close');
			jQuery("#divEditParametersPage")[0].innerHTML = divEditParametersPageForm;
		}
		
		function submitParametersPage(){
			jQuery("#form_editParameters").submit();
		}
	
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