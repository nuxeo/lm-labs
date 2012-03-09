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
      <link rel="stylesheet" type="text/css" href="${skinPath}/css/ckeditor.css"/>
        </@block>

        <@block name="scripts">
          <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
          <!--[if lt IE 9]>
            <script type="text/javascript" src="${skinPath}/js/assets/html5.js"></script>
          <![endif]-->

          <script type="text/javascript" src="${skinPath}/js/jquery/jquery-1.7.min.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery.cookie.js"></script>
	      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.form.js"></script>
	      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.validate.min.js"></script>
          <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-dropdown.js"></script>
          <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-button.js"></script>
	      <script type="text/javascript" src="${skinPath}/js/labs.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery.placeholder.min.js"></script> 
      		<script type="text/javascript" src="${skinPath}/js/timeout.js"></script>
        </@block>
        
        <style type="text/css">
		  label {
			font-weight: bold;
		  }
		</style>
    </head>
    <body>
    <#-- timeout -->
    <input type="hidden" id="serverTimeoutId" value="${serverTimeout}" />
    
    <a href="${Context.modulePath}"><img style="position: fixed; top: 0; left: 0; border: 0;z-index: 20000;" src="${skinPath}/images/beta-ribbon.png" alt="Labs-beta"></a>
    <div id="FKtopContent">
      <#include "views/common/topbar.ftl" />

      <div id="masthead">
          <img src="${skinPath}/images/default_banner.png" />
      </div>


      <div class="container" style="width: 960px;">
      	<div class="row">
         <div class="span12 columns">
	        <#assign messages = This.getMessages() />
	        <#list messages?keys as key >
	           <div class="alert alert-block alert-${key}">
	             <a class="close" data-dismiss="alert" href="#">x</a>
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
        <#include "views/common/loading.ftl">
    </div><!--FKfooter-->
<div>
&nbsp;
</div>
    </body>
</html>