<@extends src="/views/labs-base.ftl">
	<#assign isAuthorized = This.isAuthorized()>
	
	<@block name="scripts">
	  <@superBlock/>
		<script type="text/javascript" src="${skinPath}/js/sitesRoot.js"></script>
	</@block>
	
	<@block name="css">
	  <@superBlock/>
	  	<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/sitesRoot.css"/>
	</@block>
	
	<@block name="banner">
    	<div id="labssite-banner">
		    <img src="${skinPath}/images/banniere.jpg" id="bannerImgId"/> 
		</div>
    </@block>
	
	<@block name="content">	
			<div id="content" class="sitesRoot">
				<#if isAuthorized>
					<div id="linkAddSite" class="addSite">
						<a style="cursor:pointer" onClick="javascript:manageDisplayEdit();">+${Context.getMessage('label.labssite.add.site')}</a>
					</div>
				</#if>
				<#if isAuthorized>
					<div id="editSite" class="labssite" style="display: none;">
						<#include "/views/sitesRoot/editLabsSite.ftl" />
					</div>
				</#if>
				<div class="titleHomepage">
					${Context.getMessage('label.labssite.list.sites.title')}
				</div>
				<div id="MySites">
				<#list This.labsSites as n>
				    <#assign labssite = n />
					<#include "/views/sitesRoot/displayLabsSite.ftl" />
				</#list>
				</div>
			</div>
	</@block>
</@extends>		