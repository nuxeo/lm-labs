<@extends src="/views/TemplatesBase/templateCommon.ftl">

	<@block name="FKtopContent">
		<@superBlock/>
		
		<#--  masthead  -->
		<div id="masthead">
			<#--  Logo  -->
			<#include "views/common/logo.ftl" />
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
				<#--  central content -->
		        <div class="span10">
				    <#--  horizontal Navigation  -->
			      	<#include "views/common/topnavigation_area.ftl" />
			
					<#--  action-message -->
					<#include "views/common/action_message.ftl" >
					
		        	<#--  Content  -->
				    <@block name="content" />
				    
				    <#--  Commentaires  -->
				    <@block name="pageCommentable">
						<#include "/views/LabsComments/displayCommentsPage.ftl" />
					</@block>
		        </div>
				
				<#--  sidebar -->
				<div class="sidebar span2"> 
					<@block name="sidebar">
					    	<#include "views/common/sidebar_area.ftl" />
					    	<@block name="subPages" />
				    </@block>
				</div>
			
			    <div style="clear:both;"></div>
			    
			</div><#--  /row-fluid -->
		</div><#-- /container-fluid -->
	</@block>
</@extends>