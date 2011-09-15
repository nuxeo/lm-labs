<@extends src="/views/labs-base.ftl">
	<#assign isAuthorized = Session.hasPermission(Document.ref, 'Write')>
	
	<@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>
	
	<@block name="scripts">
	  <@superBlock/>
	  	<script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.14.min.js"></script>
		<script type="text/javascript" src="${skinPath}/js/Collection.js"></script>
		<script type="text/javascript" src="${skinPath}/js/PageListHeaders.js"></script>
	</@block>
	
	<@block name="css">
	  <@superBlock/>
	  	<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageList.css"/>
		<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery-ui-1.8.14.datePicker.css"/>
	</@block>
	
	<@block name="content">	
		<div id="content" class="pageList">
			<#-- COMMENT AREA --> 
  			<#include "views/common/comment_area.ftl" />
			<#if isAuthorized>
				<div id="divActionManageList">
					<button id="addLineEntry" class="btn" onClick="javascript:addLine();" title="${Context.getMessage('label.pageList.addLine')}">${Context.getMessage('label.pageList.addLine')}</button>
					<a href="#" id="displayManageList" onClick="javascript:manageList();">${Context.getMessage('label.pageList.manageList')}</a>
				</div>
				
			</#if>
			<#if isAuthorized>
				<div id="divManageList" style="display: none;">
					<#include "/views/PageList/editManageList.ftl" />
				</div>
				<div id="divEditEntryLine" style="display: none;">
					<#include "/views/PageList/editEntryLine.ftl" />
				</div>
			</#if>
			<script type="text/javascript">
				var msg_title = "${Context.getMessage('label.pageList.edit.manage.title')}";
				var label_newsHeader = "${Context.getMessage('label.pageList.edit.manage.newsHeader')}";
				var label_options = "${Context.getMessage('label.pageList.edit.editHeader.options')}";
				var label_delete_error = "${Context.getMessage('label.pageList.edit.manage.delete.error')}";
				var is_new = true;
				<#assign headers = This.getHeaders()>
				
				var headersMapBase = '(${headers.headersMapJS})';
				var headersNameBase = '${headers.headersNameJS}';
				
				<#if 2 < headers.headersMap?length >
					headersCollection.setCollection(eval( headersMapBase ), eval( '[' + headersNameBase  + ']'));
					is_new = false;
				<#else>
					addOneHeader();
				</#if>
			</script>
		</div>
	</@block>
</@extends>		