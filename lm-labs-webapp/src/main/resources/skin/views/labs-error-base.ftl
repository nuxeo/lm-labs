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
          <link rel="stylesheet/less" href="${skinPath}/less/theme/labs/specific.less">
      	  <script type="text/javascript" src="${skinPath}/js/assets/less/less-1.2.1.min.js"></script>
      	  <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.dialog2.css"/>
        </@block>

        <@block name="scripts">
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery-1.7.min.js"></script>
	      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.dialog2.js"></script>
	      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.cookie.js"></script>
        </@block>
    </head>
    <body>
    <a href="${Context.modulePath}"><img style="position: fixed; top: 0; left: 0; border: 0;z-index: 20000;" src="${skinPath}/images/beta-ribbon.png" alt="Labs-beta"></a>
    <div id="FKtopContent">

      <div id="masthead">
          <img src="${skinPath}/images/default_banner.png" />
      </div>


      <div class="container" style="width: 960px;">
      	<div class="row">
         <div class="span16 columns">
	        <#assign messages = This.getMessages() />
	        <#list messages?keys as key >
	           <div class="alert-message ${key}">
	             <a class="close" href="#">x</a>
	             ${Context.getMessage(messages[key])}
	           </div>
	        </#list>
	       </div>
	     </div>
        <@block name="content">
        </@block>
      </div>



      <div style="clear:both;"></div>

    </div><!--FKtopContent-->

    <div id="FKfooter">
        <#include "views/footer-manage-base.ftl">
    </div><!--FKfooter-->
<div>
&nbsp;
</div>
    </body>
</html>