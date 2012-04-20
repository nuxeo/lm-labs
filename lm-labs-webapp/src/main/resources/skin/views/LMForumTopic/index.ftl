<#-- assign canWrite = Session.hasPermission(Document.ref, 'Write') -->
<#assign canWrite = This.page?? && This.page.isContributor(Context.principal.name)>
<@extends src="/views/TemplatesBase/" + This.page.template.templateName + "/template.ftl">
  <#assign mySite=Common.siteDoc(Document).site />
  <@block name="title">${ mySite.title}-${This.document.title}</@block>

  <@block name="css">
    <@superBlock/>
  </@block>

  <@block name="scripts">
   	<@superBlock/>
  	
  </@block>

  <@block name="content">
  	<#include "views/common/page_header.ftl">
  </@block>
  <#include "views/LabsComments/macroComments.ftl">
  <@block name="pageCommentable">
	<#assign pageCommentable = This.getPage()/>
	<#if pageCommentable != null && pageCommentable.commentable>
		<@displayAddComment ckeditor=true pageCommentable=pageCommentable />
	</#if>
  </@block>
</@extends>