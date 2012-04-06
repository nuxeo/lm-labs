<@extends src="/views/TemplatesBase/templateCommon.ftl">
<#assign popoverPlacement = ", placement:'left'" />
	<@block name="FKtopContent">
		<@superBlock/>
		
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
				    
				    <#--  Commentaires  -->
				    <@block name="pageCommentable">
						<#include "/views/LabsComments/displayCommentsPage.ftl" />
					</@block>
		        </div>
				
				<#--  sidebar -->
				<div class="sidebar span2"> 
					<#include "views/common/sidebar_area.ftl" />
				</div>
			
			    <div style="clear:both;"></div>
			    
			</div><#--  /row-fluid -->
		</div><#-- /container-fluid -->
		
		<@block name="returnTopPage" >
			<div id="returnTopPage" style="float: left;margin-top: -19px;margin-left: 5px;">
				<a href="#" title="Haut de page"><i class="icon-arrow-up"></i></a>
			</div>
		</@block>
		
	</@block>
</@extends>