<@extends src="/views/labs-base.ftl">
	<#assign isAuthorized = This.isAuthorized()>
	
	<@block name="scripts">
	  <@superBlock/>
	  	<script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.14.min.js"></script>
		<script type="text/javascript" src="${skinPath}/js/page_news.js"></script>
	</@block>
	
	<@block name="css">
	  <@superBlock/>
	  	<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/page_news.css"/>
		<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery-ui-1.8.14.datePicker.css"/>
	</@block>
	
	<@block name="content">	
			<div id="content" class="pageNews">
				<#if isAuthorized>
					<div id="linkAddNews" class="addNews">
						<a style="cursor:pointer" onClick="javascript:manageDisplayEdit();">+${Context.getMessage('label.labsNews.add.news')}</a>
					</div>
				</#if>
				<#list This.listNews as n>
				    <#assign news = n />
					<#include "/views/PageNews/displayNews.ftl" />
				</#list>
				<#if isAuthorized>
					<div id="editNews" class="news" style="display: none;">
						<#include "/views/PageNews/editNews.ftl" />
					</div>
				</#if>
			</div>
	</@block>
</@extends>		