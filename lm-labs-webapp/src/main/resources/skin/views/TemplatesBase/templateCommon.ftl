<#assign canWrite = Session.hasPermission(Document.ref, 'Write') />
<@extends src="/views/labs-common-base.ftl">

	<@block name="css">
		<@superBlock/>
        <link rel="stylesheet/less" href="${This.path}/generated.less">
        <link rel="stylesheet" type="text/css" href="${skinPath}/css/ckeditor.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/tooltip.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.dialog2.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/pagination.css"/>
	</@block>

	<@block name="scripts">
		<@superBlock/>
		
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.controls.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.dialog2.js"></script>
        <#if canWrite>
	        <script type="text/javascript" src="${skinPath}/js/ckeditor/init.js"></script>
	        <script type="text/javascript" src="${skinPath}/js/ckeip.js"></script>
	        <script type="text/javascript" src="${skinPath}/js/ckeditor/ckeditor.js"></script>
	        <script type="text/javascript" src="${skinPath}/js/ckeditor/adapters/jquery.js"></script>
        </#if>
        <script type="text/javascript" src="${skinPath}/js/tooltip.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.hotkeys.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.pagination.js"></script>
        <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-tooltip.js"></script>
        <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-popover.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery.ThreeDots.min.leroymerlin.js"></script>
          
        <#--  ckeditor_confi -->
		<script type="text/javascript">
			<#include "views/common/ckeditor_config.ftl" />
		</script>

		<#include "views/common/subscribe_js.ftl" />
	</@block>
	
	<@block name="topbar">
		<@superBlock/>
		&nbsp;
		<#if Context.principal.isAdministrator() == true>
			<a href="${Context.modulePath}"><img style="position: fixed; top: 0; left: 0; border: 0;z-index: 20000;" src="${skinPath}/images/beta-ribbon.png" alt="Labs-beta"></a>
		</#if>
	</@block>
	
	<@block name="FKfooter">
    	<div id="FKfooter">
	        <#include "views/common/footer.ftl">
	        <#include "views/common/labsPiwik.ftl">
	        <#include "views/common/loading.ftl">
    	</div><#-- /FKfooter -->
    	<div>&nbsp;</div>
	</@block>
	
</@extends>