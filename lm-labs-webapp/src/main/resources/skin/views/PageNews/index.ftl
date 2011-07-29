<@extends src="/views/labs-base.ftl">
	<@block name="scripts">
	  <@superBlock/>
	  	<script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.14.datePicker.min.js"></script>
		<script type="text/javascript" src="${skinPath}/js/page_news.js"></script>
	</@block>
	
	<@block name="css">
	  <@superBlock/>
	  	<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/page_news.css"/>
		<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery-ui-1.8.14.datePicker.css"/>
	</@block>
	
	<@block name="content">	
			<div id="content" class="pageNews">
				<div id"linkAddNews" class="addNews">
					<a style="cursor:pointer" onClick="javascript:manageDisplayEdit();">+${Context.getMessage('label.labsNews.add.news')}</a>
				</div>
				<#list This.listNews as n>
				    <#assign news = n />
					<#include "/views/PageNews/displayNews.ftl" />
				</#list>
				<div id="editNews" class="news" style="display: none;">
					<#include "/views/PageNews/editNews.ftl" />
				</div>
			</div>
	</@block>
</@extends>		