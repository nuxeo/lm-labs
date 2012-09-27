<@extends src="/views/TemplatesBase/templateCommon.ftl">
<#assign popoverPlacement = ", placement:'left'" />
	<@block name="css">
		<@superBlock/>
		<link rel="stylesheet" href="${contextPath}/css/opensocial/light-container-gadgets.css" />
	</@block>
	<@block name="scripts">
		<@superBlock/>
        <script type="text/javascript" src="${skinPath}/js/labs-opensocial-init.js"></script>
        <script type="text/javascript" >
jQuery(document).ready(function() {
	setOpensocialOptions('${contextPath}/', '${Context.locale.language}');
	initOpensocialGadgets();
});
        </script>
	</@block>
	<@block name="FKtopContent">
		<@superBlock/>
		
		<#--  masthead  -->
		<div id="masthead">
			<#--  Logo  -->
			<#include "views/common/logo.ftl" />
			<div class="linkDOMI"><a href="${Context.modulePath}/${mySite.URL}/Page-html/Presentation-de-la-DOMI">Qui sommes-nous ?</a></div>
			<#--  horizontal Navigation  -->
			<#include "views/common/topnavigation_area.ftl" />
		</div>
		
		<#include "views/common/banner.ftl" />
		
		<#--  content -->
		<div class="container-fluid">
			<div class="row-fluid">
				<#--  central content -->
		        <div class="central span 9" style="width: 750px;margin-right: 12px;">
			
					<#--  action-message -->
					<#include "views/common/action_message.ftl" >
					
		        	<#--  Content  -->
				    <@block name="content" />
				    
				    <#--  Tags --> 
					<@block name="pageTags">
					   	<#include "views/common/labsTags.ftl">
					</@block>
				    
				    <#--  Commentaires  -->
					<@block name="pageCommentable">
					   	<#include "views/LabsComments/macroComments.ftl">
						<@displayAddComment pageCommentable=This.page />
					</@block>
		        </div>
				
				<#--  sidebar -->
				<div class="sidebar span3" style="width: 214px;">
					<div>
						<#include "views/TemplatesBase/domi/sidebar_area.ftl" />
						gras tototo
					</div>
				</div>
			
			    <div style="clear:both;"></div>
			    
			</div><#--  /row-fluid -->
		</div><#-- /container-fluid -->
		
	</@block>
  <@block name="bottom-page-js" >
    <@superBlock />
    <script type="text/javascript" src="${contextPath}/opensocial/gadgets/js/rpc.js?c=1"></script>
    <#--
    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.fancybox-1.3.4.pack.js"></script>
    <script type="text/javascript" language="javascript" src="${contextPath}/opensocial/gadgets/js/rpc:pubsub:lmselectvalue.js?c=1"></script>
    <script type="text/javascript" src="${skinPath}/js/register_rpc_show_fancybox.js"></script>
    -->
    <script type="text/javascript" src="${skinPath}/js/register_rpc_navigateto.js"></script>
    <script type="text/javascript" src="${contextPath}/js/?scripts=opensocial/cookies.js|opensocial/util.js|opensocial/gadgets.js|opensocial/cookiebaseduserprefstore.js|opensocial/jquery.opensocial.gadget.js"></script>
    <script type="text/javascript" src="${skinPath}/less/theme/Domi/js/scriptDOMI.js"></script>
    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.json-2.3.min.js"></script>
  </@block>
  
  <@block name="FKfooter">
    	<div id="FKfooter">
	        <#include "views/TemplatesBase/domi/footer.ftl">
	        <#include "views/common/labsPiwik.ftl">
	        <#include "views/common/loading.ftl">

	        	<#include "/views/common/audioReader.ftl" />

    	</div><#-- /FKfooter -->
    	<div>&nbsp;</div>
	</@block>
</@extends>