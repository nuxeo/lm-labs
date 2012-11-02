<@extends src="/views/TemplatesBase/" + This.page.template.getTemplateName() + "/template.ftl">
  <#assign isAuthorized = Session.hasPermission(Document.ref, 'Write')>

  <@block name="title">${Common.siteDoc(Document).getSite().title}-${This.document.title}</@block>

  <@block name="scripts">
    <script type="text/javascript" src="${contextPath}/wro/labs.news.js"></script>
    <@superBlock/>
  </@block>

  <@block name="css">
    <link rel="stylesheet" type="text/css" media="all" href="${contextPath}/wro/labs.news.css"/>
    <@superBlock/>
      <#include "views/common/datepicker_css.ftl">
  </@block>
  
  <@block name="docactions">
  </@block>

  <@block name="content">
      <div id="content" class="">
      		<h1>Création d'une actualité</h1>
      		<#assign news = null/>
			<#include "views/LabsNews/editProps.ftl" />
      </div>
  </@block>
  <@block name="pageTags"></@block>
  <@block name="pageCommentable"></@block>
</@extends>