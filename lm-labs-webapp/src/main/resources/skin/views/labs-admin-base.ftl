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
          <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.fancybox-1.3.4.css" /><#-- TODO still needed ?? -->
          <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery-ui-1.8.14.css"/><#-- TODO still needed ?? -->
          <link rel="stylesheet/less" href="${skinPath}/less/theme/labs/specific.less">
          <script type="text/javascript" src="${skinPath}/js/assets/less/less-1.1.4.min.js"></script>
          <link rel="stylesheet" type="text/css" href="${skinPath}/css/ckeditor.css"/>
          <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/tooltip.css"/>
          <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.dialog2.css"/>
        </@block>

        <@block name="scripts">
          <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
          <!--[if lt IE 9]>
            <script type="text/javascript" src="${skinPath}/js/assets/html5.js"></script>
          <![endif]-->

          <script type="text/javascript" src="${skinPath}/js/jquery/jquery-1.7.min.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.14.min.js"></script><#-- TODO still needed ?? -->
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery.cookie.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery.fancybox-1.3.4.js"></script><#-- TODO still needed ?? -->
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery.form.js"></script><#-- TODO still needed ?? -->
          <#--script type="text/javascript" src="${skinPath}/js/jquery/jquery.validate.min.js"></script--><#-- TODO still needed ?? -->
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery.controls.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery.dialog2.js"></script>
          <script type="text/javascript" src="${skinPath}/js/labs.js"></script>
          <script type="text/javascript" src="${skinPath}/js/ckeditor/init.js"></script>
          <script type="text/javascript" src="${skinPath}/js/ckeip.js"></script>
          <script type="text/javascript" src="${skinPath}/js/ckeditor/ckeditor.js"></script>
          <script type="text/javascript" src="${skinPath}/js/ckeditor/adapters/jquery.js"></script>
          <script type="text/javascript" src="${skinPath}/js/tooltip.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery.hotkeys.js"></script>
          <script type="text/javascript" src="${skinPath}/js/timeout.js"></script>

        </@block>
    </head>
    <body>
	<#-- timeout -->
    <input type="hidden" id="serverTimeoutId" value="${serverTimeout}" />
    
    <div id="FKtopContent">
    <#include "views/common/topbar.ftl" />

    <div id="masthead">
    <@block name="logo">
    </@block>
    <@block name="banner">
      <#include "views/common/banner.ftl" />
    </@block>
    </div>

    <div class="container">
      <@block name="breadcrumbs">
        <#include "views/common/breadcrumbs.ftl" >
      </@block>
    </div>

    <div class="container">
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
    </div>
    
    <@block name="tabs">
    </@block>

	 <script type="text/javascript">
	    <#include "views/common/ckeditor_config.ftl" />
	 </script>

    <@block name="content">
    </@block>
    
    <@block name="pageCommentable">
	    <#assign pageCommentable = This.getPage()/>
		<#if pageCommentable != null && pageCommentable.commentable>
			<#include "/views/LabsComments/displayCommentsPage.ftl" />
		</#if>
	</@block>

    <div style="clear:both;"></div>

    </div><!--FKtopContent-->

    <div id="FKfooter">
        <#include "views/common/footer.ftl">
    </div><!--FKfooter-->
<div>
&nbsp;
</div>
    </body>
</html>