<@extends src="/views/labs-base.ftl">
	<#assign isAuthorized = Session.hasPermission(Document.ref, 'Write')>
	
	<@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>
	
	<@block name="scripts">
	  <@superBlock/>
	  	<script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.14.min.js"></script>
		<script type="text/javascript" src="${skinPath}/js/PageList.js"></script>
	</@block>
	
	<@block name="css">
	  <@superBlock/>
	  	<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageList.css"/>
		<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery-ui-1.8.14.datePicker.css"/>
	</@block>
	
	<@block name="content">	
			<div id="content" class="pageList">
				
      			<#include "views/common/comment_area.ftl" />
				<#if isAuthorized>
					<div id="divActionManageList">
						<button id="addLineEntry" onClick="javascript:addLine();" title="${Context.getMessage('label.pageList.addLine')}">${Context.getMessage('label.pageList.addLine')}</button>
						<a href="#" id="displayManageList" onClick="javascript:manageList();">${Context.getMessage('label.pageList.manageList')}</a>
					</div>
					
				</#if>
				<#if isAuthorized>
					<div id="divManageList" class="news" style="display: none;">
						<#include "/views/PageList/editManageList.ftl" />
					</div>
				</#if>
				
			</div>
	</@block>
</@extends>		