<@extends src="/views/labs-base.ftl">
	<#assign isAuthorized = This.isAuthorized()>
	
	<@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>
	
	<@block name="scripts">
	  <@superBlock/>
	  	<script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.14.min.js"></script>
		<script type="text/javascript" src="${skinPath}/js/sitesRoot.js"></script>
		<script type="text/javascript" src="${skinPath}/js/jquery/jquery.form.js"></script>
		<script type="text/javascript" src="${skinPath}/js/jquery/jquery.validate.min.js"></script>
	</@block>
	
	<@block name="css">
	  <@superBlock/>
	  	<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/sitesRoot.css"/>
	  	<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery-ui-1.8.14.css"/>
	</@block>
	
	<@block name="content">	
		<div id="homePage">
			<div class="titleHomepage">
				${Context.getMessage('label.labssite.homepage.title')}
			</div>
			<ul class="">
				<li class="padding_between">
					<button id="bt_create_labssite"  name="create_labssite" >${Context.getMessage('label.labssite.add.site')}</button>
				</li>
				<li class="padding_between">
					<button id="bt_display_labssite"  name="display_labssite" onClick="go('${This.path}?homepage=display');" >${Context.getMessage('label.labssite.display.site')}</button>
				</li>
				<!--<li class="padding_between">
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