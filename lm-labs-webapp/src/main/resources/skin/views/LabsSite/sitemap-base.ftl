<@extends src="/views/TemplatesBase/" + This.page.template.templateName + "/template.ftl">

  <@block name="title">${site.title}-${This.document.title}</@block>

  <@block name="scripts">
                <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
          <!--[if lt IE 9]>
            <script type="text/javascript" src="${skinPath}/js/assets/html5.js"></script>
          <![endif]-->

          <script type="text/javascript" src="${skinPath}/js/jquery/jquery-1.7.min.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.14.min.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery.cookie.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery.fancybox-1.3.4.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery.form.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery.controls.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery/jquery.dialog2.js"></script>
          <script type="text/javascript" src="${skinPath}/js/labs.js"></script>
          <script type="text/javascript" src="${skinPath}/js/ckeditor/init.js"></script>
          <script type="text/javascript" src="${skinPath}/js/ckeip.js"></script>
          <script type="text/javascript" src="${skinPath}/js/ckeditor/ckeditor.js"></script>
          <script type="text/javascript" src="${skinPath}/js/ckeditor/adapters/jquery.js"></script>
          <script type="text/javascript" src="${skinPath}/js/tooltip.js"></script>
          <script type="text/javascript" src="${skinPath}/js/jquery.ThreeDots.min.leroymerlin.js"></script>

	      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.js"></script>
	      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.edit.js"></script>
	      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.async.js"></script>
	      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.sortable.js"></script>
	      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.pagination.js"></script>
          <script type="text/javascript" src="${skinPath}/js/timeout.js"></script>
          <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-twipsy.js"></script>
          <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-popover.js"></script>
	      <script type="text/javascript">
	        jQuery(document).ready(function() {
			  new EllipsisText().init();
		    });
		  </script>
   </@block>

   <@block name="css">
      <@superBlock/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.treeview.css"/>
  </@block>

  <@block name="content">
  	<#-- timeout -->
	<input type="hidden" id="serverTimeoutId" value="${serverTimeout}" />
    <div id="content" class="container">
        <section>
          <div class="page-header">
            <h1>PLAN DU SITE 
          	<#if site?? && (Session.hasPermission(site.document.ref, 'Everything') || Session.hasPermission(site.document.ref, 'ReadWrite')) >
          	<a href="${This.path}/@views/administer_pages"><button id="adminPagesBt" class="btn danger small">${Context.getMessage('command.sitemap.goToPageAdmin')}</button></a>
          	</#if>
          	</h1>
          </div>
          <div class="row">
          <div class="span16 columns">
            <#include "views/LabsSite/sitemap_switch_control.ftl">
            <@block name="sitemap-content"/>
            <#include "views/LabsSite/sitemap_switch_control.ftl" >
          </div> <!-- /columns -->
        </div> <!-- /row -->
        </section>
  </@block>
</@extends>