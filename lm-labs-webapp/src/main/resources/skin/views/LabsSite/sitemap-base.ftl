<@extends src="/views/labs-base.ftl">

  <@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>

  <@block name="scripts">
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery-1.5.1.min.js"></script>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.js"></script>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.edit.js"></script>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.async.js"></script>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.treeview.sortable.js"></script>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.14.min.js"></script>
   </@block>

   <@block name="css">
      <@superBlock/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.treeview.css"/>
  </@block>

  <@block name="content">
    <div id="content" class="container">
        <section>
          <div class="page-header">
            <h1>PLAN DU SITE</h1>
          </div>
          <div class="row">
          <div class="span16 columns">
            <#include "views/LabsSite/sitemap_switch_control.ftl">
            <@block name="sitemap-content"/>
            <#include "views/LabsSite/sitemap_switch_control.ftl">
          </div> <!-- /columns -->
        </div> <!-- /row -->
        </section>
  </@block>
</@extends>