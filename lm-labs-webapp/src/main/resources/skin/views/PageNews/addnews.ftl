<@extends src="/views/TemplatesBase/" + This.page.template.templateName + "/template.ftl">
  <#assign isAuthorized = Session.hasPermission(Document.ref, 'Write')>

  <@block name="title">${site.title}-${This.document.title}</@block>

  <@block name="scripts">
    <@superBlock/>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.14.min.js"></script>
      <script type="text/javascript" src="${skinPath}/js/LabsNews.js"></script>
  </@block>

  <@block name="css">
    <@superBlock/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageNews.css"/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery-ui-1.8.14.datePicker.css"/>
  </@block>
  
  <@block name="docactions">
  </@block>

  <@block name="content">
      <div id="content" class="container">
      		<h1>Création d'une news</h1>
      		<#assign news = null/>
			<#include "views/LabsNews/editProps.ftl" />
      </div>
  </@block>
  <@block name="pageCommentable"></@block>
</@extends>