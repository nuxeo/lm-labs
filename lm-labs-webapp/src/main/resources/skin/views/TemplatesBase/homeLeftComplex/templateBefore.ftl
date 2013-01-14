<@extends src="/views/TemplatesBase/templateCommon.ftl">

	<@block name="FKtopContent">
		
		<#--  masthead  -->
		<div id="masthead">
			<#--  Logo  -->
			<#include "views/common/logo.ftl" />
			<#--  Banner  -->
			<#include "views/common/banner.ftl" />
		</div>
		
		<#--  content -->
		<div class="container-fluid">
			<div class="row-fluid">
				<#--  sidebar -->
				<div class="sidebar span2"> 
					<#include "views/common/sidebar_area.ftl" >
				</div>
				
				<#--  central content -->
		        <div class="central span10">
				    <#--  horizontal Navigation  -->
			      	<#include "views/common/topnavigation_area.ftl" />
			      	
					<#--  breadcrumbs  -->
					<#include "views/common/breadcrumbs.ftl" >
			
					<#--  action-message -->
					<#include "views/common/action_message.ftl" >
					
		        	<#--  Content  -->
				    <@block name="content" />
				    
				    <#--  Tags --> 
					<@block name="pageTags">
					   	<#include "views/common/labsTags.ftl">
					</@block>
				    
				    <#--  Like --> 
					<@block name="like">
					   	<#include "common/like.ftl">
					</@block>
				    
				    <#--  Commentaires  -->
					<@block name="pageCommentable">
					   	<#include "views/LabsComments/macroComments.ftl">
						<@displayAddComment pageCommentable=This.page />
					</@block>
		        </div>
			
			    <div style="clear:both;"></div>
			    
			</div><#--  /row-fluid -->
		</div><#-- /container-fluid -->
	</@block>
</@extends>