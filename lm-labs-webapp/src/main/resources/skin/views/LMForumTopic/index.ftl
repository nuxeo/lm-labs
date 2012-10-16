<#-- assign canWrite = Session.hasPermission(Document.ref, 'Write') -->
<#assign canWrite = This.page?? && This.page.isContributor(Context.principal.name)>
<@extends src="/views/TemplatesBase/" + This.page.template.getTemplateName() + "/template.ftl">
  <#assign mySite=Common.siteDoc(Document).getSite() />
  <@block name="title">${ mySite.title}-${This.document.title}</@block>

  <@block name="css">
	<link rel="stylesheet" type="text/css" media="all" href="${contextPath}/wro/labs.topic.css"/>
    <@superBlock/>
  </@block>

 <@block name="scripts">
      <script type="text/javascript" src="${contextPath}/wro/labs.topic.js"></script>
 <@superBlock/>
   	 <script type="text/javascript" src="${skinPath}/js/ckeditor/init.js"></script>
	 <script type="text/javascript" src="${skinPath}/js/ckeip.js"></script>
	 <script type="text/javascript" src="${skinPath}/js/ckeditor/ckeditor.js"></script>
	 <script type="text/javascript" src="${skinPath}/js/ckeditor/adapters/jquery.js"></script>
  </@block>

  <@block name="docactionsaddpage"></@block>
  <@block name="docactionsonpage"></@block>

 	<@block name="content">
  		<#if modify?? && modify != null>
   			<div class="page-header">
		        <h1>${topic.title} </h1>
    		</div>
    		<#assign topic=topic/>
	  		<#include "views/PageForum/editProps.ftl" />
	  	<#else>
			<div class="page-header">
		        <h1>${topic.title} </h1>
		        <h5>${Context.getMessage('label.topic.date')} ${Document['dublincore:created']?datetime?string("EEEE dd MMMM yyyy HH:mm")}</h5>
    		</div>
    		<div class="page-description">${topic.description}</div>
    		
		</#if>
  	</@block>
  	<@block name="pageTags"></@block>
	<#if modify?? && modify != null>
		<@block name="pageCommentable"/>
	<#else>
	  	<@block name="pageCommentable">
			<#include "views/LabsComments/macroComments.ftl">
			<@displayAddComment pageCommentable=This.page ckeditor=true />
			<#include "views/common/labsTags.ftl">
		</@block>
	</#if>
</@extends>
