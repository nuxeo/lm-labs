<@extends src="/views/TemplatesBase/templateCommon.ftl">

	<@block name="topbar">
		<@superBlock/>
		&nbsp;
		<div class="topJagged">&nbsp;</div>
	</@block>
	
	<@block name="FKtopContent">
		<@superBlock/>
				
		<#--  masthead  -->
		<div id="masthead">
			<#--  Logo  -->
			<#include "views/common/logo.ftl" />
		</div>
		<div class="top">
	    	<#include "views/common/sitemap_area.ftl" />
			<#include "views/common/notification_area.ftl" />
	    </div>	
		
		<#--  content -->
		<div class="container-fluid">
			<div class="row-fluid">
				
				<div id="sidebar" class="sidebar span">
				      <#include "views/TemplatesBase/supplyChain/sidebar.ftl" />
                </div>
                  
				<div class="body container">
					<#--  Navigation  -->
	            	<@block name="tabs">
					   	<#include "views/common/topnavigation_area.ftl" />
	                </@block>
			    	<#--  breadcrumbs  -->
			    	<#include "views/common/breadcrumbs.ftl" >
					<#--  action-message -->
					<#include "views/common/action_message.ftl" >
					<div class="row">
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
			    </div>
			    
			</div><#--  /row-fluid -->
		</div><#-- /container-fluid -->
	</@block>
	
	<@block name="FKfooter">
		<div id="FKfooter">
	        <#include "views/common/loading.ftl">
	        <#include "views/common/footer.ftl">
	        <#include "views/common/labsPiwik.ftl">
	    </div><#-- /FKfooter -->
		<div class="bottomJagged">&nbsp;</div>
	</@block>
</@extends>
