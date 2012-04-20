<@extends src="/views/TemplatesBase/templateCommon.ftl">

	<@block name="FKtopContent">
		<@superBlock/>
		
		<#--  masthead  -->
		<div id="masthead">
			<#--  Logo  -->
			<#--  Banner  -->
	    	<#include "views/common/banner.ftl" />
			</div>
		
		<#--  breadcrumbs  -->
		<#include "views/common/breadcrumbs.ftl" >
		
		<#--  action-message -->
		<#include "views/common/action_message.ftl" >
		
		<#--  content -->
		<div class="container-fluid">
			<div class="row-fluid">
				
				<#--  sidebar -->
				<div class="sidebar span2"> 
					<@block name="sidebar">
					    	<#include "views/common/sidebar_area.ftl" />
				    </@block>
				</div>
			    <#--
		      		<#include "views/common/topnavigation_area.ftl" />
		      	-->
				
				<#--  central content -->
		        <div class="span10">
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