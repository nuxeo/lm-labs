<@extends src="/views/TemplatesBase/" + This.page.template.templateName + "/template.ftl">

  <@block name="title">${site.title}-${This.document.title}</@block>

  <@block name="scripts">
    <@superBlock/>
    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.js"></script>
    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.edit.js"></script>
    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.async.js"></script>
    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.sortable.js"></script>
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