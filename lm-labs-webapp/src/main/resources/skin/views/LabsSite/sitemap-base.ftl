<@extends src="/views/TemplatesBase/" + This.page.template.templateName + "/template.ftl">

  <@block name="title">${site.title}-${This.document.title}</@block>

  <@block name="scripts">
    <@superBlock/>
    <script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.14.min.js"></script>
    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.js"></script>
    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.edit.js"></script>
    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.async.js"></script>
    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.sortable.js"></script>
  </@block>

  <@block name="css">
    <@superBlock/>
    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery-ui-1.8.14.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.treeview.css"/>
  </@block>

  <@block name="content">
  	<#-- timeout -->
	<input type="hidden" id="serverTimeoutId" value="${serverTimeout}" />
    <div id="content" class="">
        <section>
          <div class="page-header">
            <h1>PLAN DU SITE 
          	<#if site?? && (Session.hasPermission(site.document.ref, 'Everything') || Session.hasPermission(site.document.ref, 'ReadWrite')) >
          	<a href="${This.path}/@views/administer_pages"><button id="adminPagesBt" class="btn btn-danger btn-small">${Context.getMessage('command.sitemap.goToPageAdmin')}</button></a>
          	</#if>
          	</h1>
          </div>
          <div class="">
            <#include "views/LabsSite/sitemap_switch_control.ftl">
            <hr/>
            <@block name="sitemap-content"/>
            <hr/>
            <#include "views/LabsSite/sitemap_switch_control.ftl" >
          </div> <!-- /row -->
        </section>
    </div>
  </@block>
  <@block name="pageCommentable">
  </@block>
</@extends>