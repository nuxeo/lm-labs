<@extends src="/views/labs-base.ftl">
	<#assign isAuthorized = This.isAuthorized()>
	
	<@block name="title">${Context.module.name}</@block>
	
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
		<div id="homePage">
			<div class="titleHomepage">
				${Context.getMessage('label.labssite.homepage.title')}
			</div>
			<ul class="">
				<li class="padding_between">
					<button id="bt_display_labssite"  name="display_labssite" onClick="go('${This.path}?homepage=display');" >${Context.getMessage('label.labssite.display.site')}</button>
				</li>
<#if isAuthorized>
				<li class="padding_between">
					<button id="bt_create_labssite"  name="create_labssite" >${Context.getMessage('label.labssite.add.site')}</button>
				</li>
</#if>			
				<#--<li class="padding_between">
					<button id="bt_load_labssite"  name="load_labssite" onClick="javascript:go('${This.path}?homepage=load');" >${Context.getMessage('label.labssite.load.site')}</button>
				</li>-->
			</ul>
		</div>
		<div id="divEditSite" class="labssite" style="display: none;">
			<#include "/views/sitesRoot/editLabsSite.ftl" />
		</div>
		<script>
			function go(url){
				document.location.href=url;
			}
		</script>
	</@block>
</@extends>		