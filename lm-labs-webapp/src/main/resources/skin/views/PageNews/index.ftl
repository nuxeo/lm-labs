<#assign mySite=Common.siteDoc(Document).getSite() />
<#assign topnewsUrl = "${This.path}/@labsrss/topnews" />
<@extends src="/views/TemplatesBase/" + This.page.template.getTemplateName() + "/template.ftl">
  <#assign isAuthorized = This.page?? && This.page.isContributor(Context.principal.name)>

  <@block name="title">${mySite.title}-${This.document.title}</@block>

  <@block name="scripts">
    <@superBlock/>
      <script type="text/javascript" src="${skinPath}/js/PageNews.js"></script>
  </@block>

  <@block name="css">
    <@superBlock/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageNews.css"/>
      <#include "views/common/datepicker_css.ftl">
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
      <link href="${topnewsUrl}" rel="alternate" type="application/rss+xml" title="${Context.getMessage('label.rss.topnews.PageNews.title', This.document.title)}" />
  </@block>

  <@block name="content">
  	<#include "views/LabsNews/macroNews.ftl">
      <div id="content" class="container-fluid">

    	<#include "views/common/page_header.ftl">

        <#if isAuthorized>
        	<div class="editblock" style="width: 100%;text-align: right;margin-bottom: 5px;margin-top: 5px;">
          		<a href="${This.path}/@views/addnews?props=open" class="btn" style="margin-right: 5px;"><i class="icon-plus"></i>${Context.getMessage('label.labsNews.add.news')}</a>
          	</div>
        </#if>


		<#include "views/common/paging.ftl" />
		<#assign pp = This.newsPageProvider />
		<#assign paramaterCurrentPage = Context.request.getParameter('page') />
		<#assign currentPage = 0 />
		<#if paramaterCurrentPage?? && paramaterCurrentPage != null>
			<#assign currentPage = paramaterCurrentPage?number?long />
		</#if>
		<#assign allNews = This.getAllNews(pp.setCurrentPage(currentPage)) />


        <#list allNews as news>
          <#assign path=This.path + "/" + news.documentModel.name />
          <section class="labsnews <@generateClassNewsVisibility news=news result="editblock"/>">
	          	<div class="row-fluid" id="summaryNews${news.documentModel.ref}">
	          		<@generateSummaryNews news=news path=path withHref=true/>
				  	<#-- Collapse -->
					<div class="span1" style="margin-left: 15px;float: right;">
						<img src="${skinPath}/images/newsOpen.png" style="cursor: pointer;margin-top:5px;" onclick="javascript:openNews('${news.documentModel.ref}', '${skinPath}');"/>
					</div>
	          	</div>

	          	<div id="contentNews${news.documentModel.ref}" style="display: none;">
	              <div class="row-fluid">
	              	 <#if news.hasSummaryPicture()>
			          		<#-- Image -->
			          		<div class="span2">
			          			<img src="${This.path}/${news.documentModel.name}/summaryPictureTruncated" style="margin-top: 5px;"/>
			          		</div>
			          		<#-- Central -->
			          		<div class="span9 <@generateClassNewsVisibility news=news result="hiddenNews"/>">
			          			<@generateHeaderNews news=news path=path withHref=true withBy=true />
				            </div>
		          	<#else>
		              	<div class="span11 <@generateClassNewsVisibility news=news result="hiddenNews"/>">
			                <@generateHeaderNews news=news path=path withHref=true withBy=true />
			            </div>
			        </#if>
	                <div class="span1" style="margin-left: 15px;float: right;">
	                	<img src="${skinPath}/images/newsClose.png" style="cursor: pointer;margin-top:5px;" onclick="javascript:closeNews('${news.documentModel.ref}', '${skinPath}');"/>
	                </div>
	              </div>

				  <div id="contentContentNews${news.documentModel.ref}">
		              <@generateContentHtmlNews news=news />
          		  </div>
	          	</div>


            </section>
        </#list>
        <div style="text-align : center;">
					<@paging pageProvider=pp url=This.path+"?page=" />
					<@resultsStatus pageProvider=pp />
				</div>
        <hr />
        <a href="${topnewsUrl}" title="${Context.getMessage('tooltip.PageNews.rss')}<#if !mySite.visible> ${Context.getMessage('tooltip.PageNews.rss.siteNotPublish')}</#if>" target="_blank"><img src="${skinPath}/images/iconRss.gif"/></a>
      </div>
  </@block>
</@extends>

