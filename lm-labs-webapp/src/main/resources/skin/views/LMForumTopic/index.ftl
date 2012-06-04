<#-- assign canWrite = Session.hasPermission(Document.ref, 'Write') -->
<#assign canWrite = This.page?? && This.page.isContributor(Context.principal.name)>
<@extends src="/views/TemplatesBase/" + This.page.template.getTemplateName() + "/template.ftl">
  <#assign mySite=Common.siteDoc(Document).getSite() />
  <@block name="title">${ mySite.title}-${This.document.title}</@block>

  <@block name="css">
    <@superBlock/>
     <link rel="stylesheet" type="text/css" href="${skinPath}/css/ckeditor.css"/>
  </@block>

 <@block name="scripts">
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
    		</div>
    		<div class="page-description">${topic.description}</div>
    		
		</#if>
  	</@block>
	<#if modify?? && modify != null>
		<@block name="pageCommentable"/>
	<#else>
		<#include "views/LabsComments/macroComments.ftl">
	  	<@block name="pageCommentable">
			<#assign pageCommentable = This.getPage()/>
			<#if pageCommentable != null && pageCommentable.commentable>
				<@displayAddComment ckeditor=true pageCommentable=pageCommentable />
			</#if>
		</@block>
	</#if>
</@extends>