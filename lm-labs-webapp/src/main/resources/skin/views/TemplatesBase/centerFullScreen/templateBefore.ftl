<@extends src="/views/TemplatesBase/templateCommon.ftl">

	<@block name="FKtopContent">
		<@superBlock/>
		
		<#--  masthead  -->
		<div id="masthead">
			<#--  Logo  
			<#include "views/common/logo.ftl" /> -->
			<#--  Banner  -->
			<#include "views/common/banner.ftl" />
		</div>
        <div class="top">
            <#include "views/common/sitemap_area.ftl" />
            <#include "views/common/notification_area.ftl" />
        </div>  
		
		<#--  content -->
		<div class="container-fluid">
			<div class="row-fluid">
				<#--  sidebar 
				<div class="sidebar span2"> 
					<@block name="sidebar">
					    	<#include "views/common/sidebar_area.ftl" />
				    </@block>
				</div> -->
				
				<#--  central content -->
		        <div class="span12">
		        
				    <#--  horizontal Navigation  -->
			      	<#include "views/common/topnavigation_area.ftl" />
		
					<#--  breadcrumbs  -->
					<#include "views/common/breadcrumbs.ftl" >
			
					<#--  action-message -->
					<#include "views/common/action_message.ftl" >
					
		        	<#--  Content  -->
				    <@block name="content" />
				    
				    <#--  Commentaires  -->
				   	<#include "views/LabsComments/macroComments.ftl">
					<@block name="pageCommentable">
						<#assign pageCommentable = This.getPage()/>
						<#if pageCommentable != null && pageCommentable.commentable>
							<@displayAddComment ckeditor=false pageCommentable=pageCommentable />
						</#if>
					</@block>
		        </div>
			
			    <div style="clear:both;"></div>
			    
			</div><#--  /row-fluid -->
		</div><#-- /container-fluid -->
	</@block>
</@extends>