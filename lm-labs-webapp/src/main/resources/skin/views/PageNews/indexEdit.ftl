<@extends src="/views/labs-base.ftl">
	<#assign isAuthorized = Session.hasPermission(Document.ref, 'Write')>
	
	<@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>
	
	<@block name="scripts">
	  <@superBlock/>
	  	<script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.14.min.js"></script>
		<script type="text/javascript" src="${skinPath}/js/PageNews.js"></script>
	</@block>
	
	<@block name="css">
	  <@superBlock/>
	  	<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageNews.css"/>
		<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery-ui-1.8.14.datePicker.css"/>
	    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
	</@block>
	
	<@block name="content">	
			<div id="content" class="pageNews">
				<#if isAuthorized>
					<div id="editNews" class="news" style="display: none;">
						<#include "/views/PageNews/editNews.ftl" />
					</div>
				</#if>
			</div>
	</@block>
</@extends>		