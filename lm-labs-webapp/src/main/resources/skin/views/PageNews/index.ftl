<@extends src="/views/labs-base.ftl">
	<#assign isAuthorized = Session.hasPermission(Document.ref, 'Write')>
	
	<@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>
	
	<@block name="scripts">
	  <@superBlock/>
	  	<script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.14.min.js"></script>
		<script type="text/javascript" src="${skinPath}/js/PageNews.js"></script>
		<script type="text/javascript" src="${skinPath}/js/ckeditor/ckeditor.js"></script>
		<script type="text/javascript" src="${skinPath}/js/ckeditor/init.js"></script>
	</@block>
	
	<@block name="css">
	  <@superBlock/>
	  	<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageNews.css"/>
		<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery-ui-1.8.14.datePicker.css"/>
	    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
		<link rel="stylesheet" type="text/css" href="${skinPath}/css/ckeditor.css"/>
	</@block>
	
	<@block name="content">	
			<div id="content" class="pageNews">
				<#-- COMMENT AREA --> 
      			<#include "views/common/comment_area.ftl" />
				<#if isAuthorized>
					<div id="linkAddNews" class="addNews"onClick="javascript:manageDisplayEdit();">+${Context.getMessage('label.labsNews.add.news')}</div>
				</#if>
				<#if isAuthorized>
					<div id="editNews" class="news" style="display: none;">
						<#include "/views/PageNews/editNews.ftl" />
					</div>
				</#if>
				<#list This.listNews as n>
				    <#assign news = n />
					<#include "/views/PageNews/displayNews.ftl" />
				</#list>
			</div>
	</@block>
</@extends>		