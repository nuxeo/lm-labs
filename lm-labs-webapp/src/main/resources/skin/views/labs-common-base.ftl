<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
    "http://www.w3.org/TR/html4/strict.dtd">
<html lang="fr">
<#assign popoverPlacement = "" />
    <head>
        <@block name="meta">
          <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        </@block>

        <title>
            <@block name="title">Labs</@block>
        </title>

        <@block name="css">
          <link rel="icon" type="image/x-icon" href="/nuxeo/img/logo.jpeg" />
          <link rel="shortcut icon"  type="image/x-icon" href="/nuxeo/img/logo.jpeg"/>
          <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/tooltip.css"/>
          <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.dialog2.css"/>
        </@block>

        <@block name="scripts">
          <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
          <!--[if lt IE 9]>
            <script type="text/javascript" src="${skinPath}/js/assets/html5.js"></script>
          <![endif]-->
          
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery-1.7.min.js"></script>
      	  <script type="text/javascript" src="${skinPath}/js/jquery/jquery.controls.js"></script>
      	  <script type="text/javascript" src="${skinPath}/js/jquery/jquery.dialog2.js"></script>
	      <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-tooltip.js"></script>
          <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-popover.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery.cookie.js"></script>
	      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.form.js"></script>
          <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-dropdown.js"></script>
          <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-button.js"></script>
      	  <script type="text/javascript" src="${skinPath}/js/scroll-startstop.events.jquery.js"></script>
	      <script type="text/javascript" src="${skinPath}/js/labs.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery.placeholder.min.js"></script> 
      	  <script type="text/javascript" src="${skinPath}/js/timeout.js"></script>
          <script type="text/javascript" src="${skinPath}/js/assets/less/less-1.2.1.min.js"></script>

        </@block>
    </head>
    
    <body>
    	
		<#-- timeout -->
	    <input type="hidden" id="serverTimeoutId" value="${serverTimeout}" />
	    
		<@block name="topbar">
			<#include "views/common/topbar.ftl" />
		</@block>
		
		<div id="FKtopContent" style="position: relative;">
			<@block name="FKtopContent" />
		</div><#-- /FKtopContent -->
		
		<div style="display:none;" class="nav_up" id="nav_up"></div>
		<div style="display:none;" class="nav_down" id="nav_down"></div>
		
		<@block name="FKfooter" />
		
		<@block name="bottom-page-js" >
			<#include "views/common/topbar_js.ftl" />
			<#if (mySite?? && mySite.isContributor(Context.principal.name) ) >
	        	<script type="text/javascript" src="${skinPath}/js/page_parameters.js"></script>
	        </#if>
	        <#if mySite?? && mySite.isContributor(Context.principal.name) && This.page?? && !(mySite.homePageRef == This.page.document.id) >
	        	<script type="text/javascript" src="${skinPath}/js/setHomePage.js"></script>
	        </#if>
		</@block>
    </body>
</html>