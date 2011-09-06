<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
    "http://www.w3.org/TR/html4/strict.dtd">
<html lang="fr">
    <head>
        <@block name="meta">
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <meta name="gwt:property" content="locale=fr">
        <base href="${Context.basePath}/labssites" />
        </@block>

        <title>
            <@block name="title">
            Labs
            </@block>
        </title>
        
        <@block name="css">
        <link rel="icon" type="image/x-icon" href="/nuxeo/img/logo.jpeg" />
        <link rel="shortcut icon"  type="image/x-icon" href="/nuxeo/img/logo.jpeg"/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/labssite.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/main.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.fancybox-1.3.4.css" />
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery-ui-1.8.14.css"/>
        
        
        
        
        <link rel="stylesheet/less" href="${skinPath}/less/bootstrap/bootstrap.less">
		<script src="${skinPath}/js/assets/less/less-1.1.4.min.js"></script>
        </@block>

        <@block name="scripts">
        	    <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
	    <!--[if lt IE 9]>
	      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
	    <![endif]-->
	        <script type="text/javascript" src="${skinPath}/js/jquery/jquery-1.5.1.min.js"></script>
	        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.cookie.js"></script>
	        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.fancybox-1.3.4.js"></script>
	        <script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.14.min.js"></script>
			<script type="text/javascript" src="${skinPath}/js/jquery/jquery.form.js"></script>
			<script type="text/javascript" src="${skinPath}/js/jquery/jquery.validate.min.js"></script>
			<script type="text/javascript" src="${skinPath}/js/labs.js"></script>
        </@block>

        

    </head>
    <body>
        <div id="FKtopContent">
            
            
            
    <div class="topbar">
      <div class="fill">
        <div class="container">
          <h3><a href="#"><#if site??>${site.title}</#if></a></h3>
          <ul>             
          </ul>
          
          <ul class="nav secondary-nav">
          <li><form>
            <input class="normal" placeholder="Rechercher dans le site" name="q"/>
            </form></li>            
            <li><#include "common/login.ftl" /></li>
            <#if Context.principal.isAnonymous() == false>           
            <li class="menu">
              <a href="#" class="menu">${Context.principal.firstName} ${Context.principal.lastName}</a>
              <ul class="menu-dropdown">
                <@block name="docactions"></@block>
                <@block name="siteactions"></@block>                                
                <li><a id="logout" href="#">Déconnexion</a></li>
              </ul>
            </li>
            </#if>
          </ul>
        </div>
      </div><!-- /fill -->
    </div><!-- /topbar -->
    &nbsp;
    
    <div id="masthead">
            <@block name="banner">
    	<#include "views/common/banner.ftl" />
            	
            </@block>
    </div>
    

            <@block name="breadcrumbs">
                <#include "views/common/breadcrumbs.ftl">
            </@block>

        
        
        
           <@block name="content"></@block>
           <div style="clear:both;"></div>
    	</div><!--FKtopContent-->
    	
    	
    	
    	
        
        <div id="FKfooter">

            <div id="FKfooter_logo_lm">&nbsp;</div>
            <@block name="footer">
              <a href="/nuxeo/pdf/charte.pdf" target="_blank">${Context.getMessage('label.information')}</a> | ${Context.getMessage('label.contact')} : <a href="mailto:communicationinterne@leroymerlin.fr">communicationinterne@leroymerlin.fr</a>
            </@block>

            <div style="clear:both;"></div>

            <div id="FKfooter_bottom">
                <div id="FKfooter_logo_adeo"></div>
            </div>
        </div><!--FKfooter-->

    </body>
</html>