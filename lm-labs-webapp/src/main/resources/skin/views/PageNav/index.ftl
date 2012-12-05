<#assign mySite=Common.siteDoc(Document).getSite() />
<@extends src="/views/TemplatesBase/" + mySite.template.getTemplateName() + "/template.ftl">
  <#assign isAuthorized = Session.hasPermission(Document.ref, 'Write')>

  <@block name="title">${mySite.title}-${This.document.title}</@block>

  <@block name="scripts">
    <script type="text/javascript" src="${contextPath}/wro/labs.pagenav.js"></script> <#--MUST BE BEFORE superBlock -->
    <@superBlock/>
    <script type="text/javascript" >
      <#--jQuery(document).ready(function() {
        jQuery('.news-navigation a[rel=popover]').popover({offset: 10, html: true});
      });-->
    </script>
  </@block>

  <@block name="css">
    <link rel="stylesheet" type="text/css" media="all" href="${contextPath}/wro/labs.news.css"/>
    <@superBlock/>
    <#--<#include "views/common/datepicker_css.ftl">-->
  </@block>


  <@block name="content">
    <div id="divPageNav" class="container-fluid">
      	<#include "views/common/page_header.ftl">
      	<div class="container-fluid" style="margin-top: 10px;margin-bottom: 10px;">
      		<#assign listTags = pageNav.getTags() />
			<#list listTags as tag><span class="label">${tag}</span>&nbsp;</#list>
		</div>
      	<#if isAuthorized>
        	<div class="editblock" style="margin-top: -33px;width: 100%;text-align: right;float: right;">
				<a id="btnModifyPropsPageNav" class="btn" style="cursor: pointer;margin-right: 5px;" onclick="javascript:actionPropsPageNav();"><i class="icon-eye-open"></i>Modifier les critères</a>
		  	</div>
		  	<#include "views/PageNav/editProps.ftl" />
        </#if>
        
        <#include "views/common/paging.ftl" />
		<#assign pp = This.taggetPageProvider />
		<#assign paramaterCurrentPage = Context.request.getParameter('page') />
		<#assign currentPage = 0 />
		<#if paramaterCurrentPage?? && paramaterCurrentPage != null>
			<#assign currentPage = paramaterCurrentPage?number?long />
		</#if>
		
        <#if pp != null>
	        <#list This.getTaggedPage(pp.setCurrentPage(currentPage)) as page>
	        	<#if This.pageAsPreview(page) >
	        		<section class="labsnews">
		        		<#include "views/" + page.document.type +"/previewNav.ftl" />
	        	<#else>
		        	<section class="labsnews">
		        		<#include "views/LabsPage/previewNav.ftl" />
		        </#if>
		        <#assign thisPage = page />
        		<#if (page.document.type == "LabsNews") >
					<#assign thisPage = This.getLabsNewsAdapter(page.document) />
				</#if>
				<#if (thisPage && thisPage != null) >
					<#assign listTags=thisPage.getLabsTags()>
					<div id="divDisplayTags" class="container-fluid">
						<i class="icon-tags">&nbsp;:&nbsp;<#list listTags as tag>${tag}<#if (listTags?last != tag)>, </#if></#list></i>
					</div>
				</#if>
					<br/>
					</section>
	        </#list>
	        <div style="text-align : center;">
				<@paging pageProvider=pp url=This.path+"?page=" />
				<@resultsStatus pageProvider=pp />
			</div>
		</#if>
        <hr />
        
    </div>

  </@block>
  
</@extends>