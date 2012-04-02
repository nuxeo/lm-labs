<#assign mySite = Common.siteDoc(Document).site />
<#-- @extends src="/views/TemplatesBase/" + mySite.template.templateName + "/template.ftl" -->
<@extends src="/views/labs-admin-base.ftl">
<#assign isHomePage = This.page?? && (mySite.homePageRef == This.page.document.id) />
  <@block name="title">${mySite.title}-${This.document.title}-Permissions</@block>


  <#-- @block name="docactionsaddpage"></@block>
  <@block name="docactionsonpage"></@block -->


  <@block name="content">
  	<div class="page-header">
    	<h1>Permissions de la page ${This.document.title} &nbsp;</h1>
    	<div style="position: relative; float: right;margin-top: -19px;">
    		<a href="${This.path}" class="btn btn-mini btn-primary"><i class="icon-share-alt"></i>Retour</a>
    		<#if !isHomePage>
    			<a href="javascript:blockInherits('');" class="btn btn-mini">Bloquer l'héritage</a>
    			<a href="javascript:unblockInherits('');" class="btn btn-mini">Débloquer l'héritage parent</a>
    		</#if>
    	</div>
    </div>
	<script type="text/javascript">
	function blockInherits(permission){
		jQuery('#waitingPopup').dialog2('open');
		jQuery.ajax({
			type: 'GET',
		    async: false,
		    url: "${This.path}/@labspermissions/blockInherits?permission=" + permission,
		    success: function(data) {
		    	jQuery('#waitingPopup').dialog2('close');
		    	jQuery("#divDislayArray")[0].innerHTML = '<img src="${skinPath}/images/loading.gif" />';
				jQuery("#divDislayArray").load('${This.path}/@labspermissions');
				initModalLabsPermissions();
		    },
		    error: function(data){
		    	jQuery('#waitingPopup').dialog2('close');
		    }
		});
	}
	
	function unblockInherits(permission){
		jQuery('#waitingPopup').dialog2('open');
		jQuery.ajax({
			type: 'GET',
		    async: false,
		    url: "${This.path}/@labspermissions/unblockInherits?permission=" + permission,
		    success: function(data) {
		    	jQuery('#waitingPopup').dialog2('close');
		    	jQuery("#divDislayArray")[0].innerHTML = '<img src="${skinPath}/images/loading.gif" />';
				jQuery("#divDislayArray").load('${This.path}/@labspermissions');
				initModalLabsPermissions();
		    },
		    error: function(data){
		    	jQuery('#waitingPopup').dialog2('close');
		    }
		});
	}
	
	</script>
  	<#include "views/LabsPermissions/permissionsManager.ftl" >
  </@block>
  
  
</@extends>