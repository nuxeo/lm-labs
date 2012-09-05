<@extends src="/views/TemplatesBase/templateCommon.ftl">
<#assign popoverPlacement = ", placement:'left'" />
	<@block name="FKtopContent">
		<@superBlock/>
		
		<#--  masthead  -->
		<div id="masthead">
			<#--  Logo  -->
			<#include "views/common/logo.ftl" />
			<#--  horizontal Navigation  -->
			<#include "views/common/topnavigation_area.ftl" />
			<#--  Banner  -->
			<#include "views/common/banner.ftl" />
		</div>
		
		<#--  content -->
		<div class="container-fluid">
			<div class="row-fluid">
				<#--  central content -->
		        <div class="central span10">

				    
			      	
					
			
					<#--  action-message -->
					<#include "views/common/action_message.ftl" >
					
		        	<#--  Content  -->
				    <@block name="content" />
				    
				    <#--  Commentaires  -->
					<@block name="pageCommentable">
					   	<#include "views/LabsComments/macroComments.ftl">
						<@displayAddComment pageCommentable=This.page />
					</@block>
		        </div>
				
				<#--  sidebar -->
				<div class="sidebar span2"> 
					<#include "views/common/sidebar_area.ftl" />
				</div>
			
			    <div style="clear:both;"></div>
			    
			</div><#--  /row-fluid -->
		</div><#-- /container-fluid -->
		
	</@block>
</@extends>