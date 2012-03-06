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
          <link rel="stylesheet/less" href="${This.path}/generated.less">
          <script type="text/javascript" src="${skinPath}/js/assets/less/less-1.2.1.min.js"></script>
          <link rel="stylesheet" type="text/css" href="${skinPath}/css/ckeditor.css"/>
          <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/tooltip.css"/>
          <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.dialog2.css"/>
          <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/pagination.css"/>
        </@block>

        <@block name="scripts">
          <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
          <!--[if lt IE 9]>
            <script type="text/javascript" src="${skinPath}/js/assets/html5.js"></script>
          <![endif]-->

          <script type="text/javascript" src="${skinPath}/js/jquery/jquery-1.7.min.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery.cookie.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery.controls.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery.dialog2.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery.form.js"></script>
          <script type="text/javascript" src="${skinPath}/js/labs.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery.placeholder.min.js"></script> 
          <script type="text/javascript" src="${skinPath}/js/ckeditor/init.js"></script>
          <script type="text/javascript" src="${skinPath}/js/ckeip.js"></script>
          <script type="text/javascript" src="${skinPath}/js/ckeditor/ckeditor.js"></script>
          <script type="text/javascript" src="${skinPath}/js/ckeditor/adapters/jquery.js"></script>
          <script type="text/javascript" src="${skinPath}/js/tooltip.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery.hotkeys.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery.pagination.js"></script>
          <script type="text/javascript" src="${skinPath}/js/timeout.js"></script>
          <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-tooltip.js"></script>
          <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-popover.js"></script>
          <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-dropdown.js"></script>
          <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-button.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery.ThreeDots.min.leroymerlin.js"></script>
          <script type="text/javascript">
            jQuery(document).ready(function() {
			  new EllipsisText().init();
              jQuery('#waitingPopup').dialog2({autoOpen : false, closeOnOverlayClick : false, removeOnClose : false, showCloseHandle : false});
		    });
		  </script>

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
    </@block>
    </div>

    <div class="container">
    <div class="row-fluid">
                  <@block name="tabs">
                        <#include "views/common/topnavigation_area.ftl" />
                  </@block>
    </div>
    <div class="row-fluid">
      <@block name="breadcrumbs">
        <#include "views/common/breadcrumbs.ftl" >
      </@block>
    </div>
    </div>

    <div class="container">
      <div class="row">
        <#assign messages = This.getMessages() />
        <#list messages?keys as key >
           <div class="alert alert-block alert-${key}">
             <a class="close" data-dismiss="alert" href="#">&times;</a>
             ${Context.getMessage(messages[key])}
           </div>
        </#list>
      </div>
    </div>
    
	 <script type="text/javascript">
	    <#include "views/common/ckeditor_config.ftl" />
	 </script>
	 
	 <div class="container-fluid">
     <div class="row-fluid">

		<div class="sidebar span2"> 
		<@block name="sidebar">
		    	<#include "views/TemplatesBase/bootswatch/sidebar.ftl" />
	    </@block>
		</div>
	
        <div class="span10"> 
	    <@block name="content">
	    </@block>
	    <@block name="pageCommentable">
		    <#assign pageCommentable = This.getPage()/>
			<#if pageCommentable != null && pageCommentable.commentable>
				<#include "/views/LabsComments/displayCommentsPage.ftl" />
			</#if>
		</@block>
        </div>
	
	    <div style="clear:both;"></div>
	
	    </div><!--FKtopContent-->
    
    </div><!--row-fluid-->
    </div><!--container-fluid-->

    <div id="FKfooter">
        <#include "views/common/loading.ftl">
        <#include "views/common/footer.ftl">
        <#include "views/common/labsPiwik.ftl">
    </div><!--FKfooter-->
<div>
&nbsp;
</div>
    </body>
    
</html>