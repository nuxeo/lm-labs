<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
    "http://www.w3.org/TR/html4/strict.dtd">
<html lang="fr">
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
		
		<div id="FKtopContent">
			<@block name="FKtopContent" />
		</div><#-- /FKtopContent -->
		
		<@block name="FKfooter" />
		
    </body>
</html>