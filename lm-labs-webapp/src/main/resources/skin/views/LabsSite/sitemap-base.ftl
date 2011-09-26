<@extends src="/views/labs-base.ftl">

  <@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>

  <@block name="scripts">
                <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
          <!--[if lt IE 9]>
            <script type="text/javascript" src="${skinPath}/js/assets/html5.js"></script>
          <![endif]-->

          <script type="text/javascript" src="${skinPath}/js/jquery/jquery-1.5.1.min.js"></script>
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
    <div id="content" class="container">
        <section>
          <div class="page-header">
            <h1>PLAN DU SITE</h1>
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