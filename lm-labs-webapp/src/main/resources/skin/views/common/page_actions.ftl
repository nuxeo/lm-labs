<#assign page = This.page/>
<#if page?? >
<script type="text/javascript">
jQuery(document).ready(function() {
	jQuery("#divEditParametersPage").dialog2({
		autoOpen : false,
		closeOnOverlayClick : false,
		removeOnClose : false,
		showCloseHandle : false,
	});
});
		
function subscribePage(subscribe) {
	jQuery.ajax({
		type: 'GET',
	    async: false,
	    url: "${This.path}/@" + (subscribe ? 'subscribe' : 'unsubscribe'),
	    success: function(data) {
	    	if (subscribe) {
	    		jQuery('#subscribeBt').hide();
	    		jQuery('#unsubscribeBt').show();
	        }
	        else {
	    		jQuery('#subscribeBt').show();
	    		jQuery('#unsubscribeBt').hide();
	        }
	    },
	    error: function() {
    		<#-- TODO alert
	    	console.log('subscribe failed.');
	        -->
	    }
	});
	return false;
}
		
var divEditParametersPageForm;
		
function setAsHomePage() {
	jQuery.ajax({
		type: 'PUT',
	    async: false,
	    url: '${This.path}/@setHome',
	    success: function(data) {
	    	document.location.href = '${This.path}';
	    }
	});
}

function openParametersPage(){
	jQuery("#divEditParametersPage").dialog2('open');
	divEditParametersPageForm = jQuery("#divEditParametersPage")[0].innerHTML;
}

function closeParametersPage(){
	jQuery("#divEditParametersPage").dialog2('close');
	jQuery("#divEditParametersPage")[0].innerHTML = divEditParametersPageForm;
}

function submitParametersPage(){
	if(jQuery("#publishPage").attr("checked")=="checked") {
		publishPage();
	} else {
		draftPage();
	}
	jQuery("#form_editParameters").submit();
}

function publishPage(){
	//if (confirm("${Context.getMessage('label.lifeCycle.page.wouldYouPublish')}")){
		jQuery.ajax({
			type: 'PUT',
		    async: false,
		    url: '${This.path}/@labspublish/publish',
		    success: function(data) {
		    	/*if (data == 'publish') {
		          alert("${Context.getMessage('label.lifeCycle.page.hasPublished')}");
		          document.location.href = '${This.path}';
		        }
		        else {
		          alert("${Context.getMessage('label.lifeCycle.page.hasNotPublished')}");
		        }*/
		    }
		});
	//}
}

function draftPage(){
	//if (confirm("${Context.getMessage('label.lifeCycle.page.wouldYouDraft')}")){
		jQuery.ajax({
			type: 'PUT',
		    async: false,
		    url: '${This.path}/@labspublish/draft',
		    success: function(data) {
		    	/*if (data == 'draft') {
		          alert("${Context.getMessage('label.lifeCycle.page.hasDrafted')}");
		          document.location.href = '${This.path}';
		        }
		        else {
		          alert("${Context.getMessage('label.lifeCycle.page.hasNotDrafted')}");
		        }*/
		    }
		});
	//}
}

function deletePage(){
	if (confirm("${Context.getMessage('label.lifeCycle.page.wouldYouDelete')}")){
		jQuery.ajax({
			type: 'PUT',
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
	<#--if Common.getNotifiableTypes()?seq_contains(Document.type) >
	<li>
		<a id="subscribeBt" <#if This.isSubscribed() >style="display:none;"</#if> href="#" onclick="javascript:return subscribePage(true);">
		<#if Document.type == "PageNews">
			${Context.getMessage('command.contextmenu.PageNews.subscribe')}
		<#else>
			${Context.getMessage('command.contextmenu.Page.subscribe')}
		</#if>
		</a>
		<a id="unsubscribeBt" <#if !This.isSubscribed() >style="display:none;"</#if> href="#" onclick="javascript:return subscribePage(false);">
		<#if Document.type == "PageNews">
			${Context.getMessage('command.contextmenu.PageNews.unsubscribe')}
		<#else>
			${Context.getMessage('command.contextmenu.Page.unsubscribe')}
		</#if>
		</a>
	</li>
	</#if-->
	<#if Session.hasPermission(This.document.ref, "Everything")>
	     <#--if page.visible>
			<li><a href="#" onclick="javascript:draftPage();">${Context.getMessage('command.docactions.draft')}</a></li>
		<#else>
			<li><a href="#" onclick="javascript:publishPage();">${Context.getMessage('command.docactions.publish')}</a></li>
		</#if-->
		<!--   delete     -->
		<#--li><a href="#" onclick="javascript:deletePage();">${Context.getMessage('command.docactions.delete')}</a></li-->
		<!--   Manage parameter's page     -->
		<li><a href="#" onclick="javascript:openParametersPage();">${Context.getMessage('command.docactions.parameters')}</a></li>
		<div id="divEditParametersPage" style="display: none;">
			<#include "views/common/page_parameters.ftl" />
		</div>
		<li>
			<#if !(Common.siteDoc(Document).site.indexDocument.id == page.document.id)>
				<a href="#" onclick="javascript:setAsHomePage();" >${Context.getMessage('command.docactions.homePage')}</a>
			</#if>
		</li>
		
	</#if>
</#if>
