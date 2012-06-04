<@extends src="/views/TemplatesBase/" + This.page.template.getTemplateName() + "/template.ftl">
  <#assign isAuthorized = Session.hasPermission(Document.ref, 'Write')>

  <@block name="title">${Common.siteDoc(Document).getSite().title}-${This.document.title}</@block>

  <@block name="scripts">
    <@superBlock/>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.ui.datepicker-fr.js"></script>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.18.datepicker.min.js"></script>
      <script type="text/javascript" src="${skinPath}/js/LabsNews.js"></script>
  </@block>

  <@block name="css">
    <@superBlock/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageNews.css"/>
      <#include "views/common/datepicker_css.ftl">
  </@block>
  
  <@block name="docactions">
  </@block>

  <@block name="content">
      <div id="content" class="">
      		<h1>Création d'un topic</h1>
      		<#assign topic = null/>
			<#include "views/PageForum/editProps.ftl" />
      </div>
  </@block>
  <@block name="pageCommentable"></@block>
</@extends>